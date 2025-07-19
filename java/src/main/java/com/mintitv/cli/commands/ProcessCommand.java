package com.mintitv.cli.commands;

import com.mintitv.api.exceptions.MintApiException;
import com.mintitv.api.models.*;
import com.mintitv.api.process.ProcessImagePoolService;
import com.mintitv.api.core.validation.FileValidator;
import com.mintitv.api.core.validation.ValidationUtils;
import com.mintitv.cli.CommandLineParser;
import com.mintitv.cli.core.BaseCommand;
import com.mintitv.common.Constants;
import com.mintitv.common.Messages;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * Process command implementation for processing document images.
 */
public class ProcessCommand extends BaseCommand {
    
    private static final String COMMAND_NAME = "process";
    private static final String COMMAND_USAGE = "mintitv-cli process [options] <file1> [file2 ...]";
    
    private final ProcessImagePoolService processService;
    private final FileValidator fileValidator;
    private final ObjectMapper objectMapper;
    
    public ProcessCommand() {
        this.processService = new ProcessImagePoolService();
        this.fileValidator = new FileValidator();
        this.objectMapper = new ObjectMapper();
    }
    
    public ProcessCommand(ProcessImagePoolService processService, 
                         FileValidator fileValidator,
                         ObjectMapper objectMapper) {
        this.processService = processService;
        this.fileValidator = fileValidator;
        this.objectMapper = objectMapper;
    }
    
    @Override
    public String getName() {
        return COMMAND_NAME;
    }
    
    @Override
    public String getDescription() {
        return Messages.PROCESS_DESCRIPTION;
    }
    
    @Override
    protected void doExecute(CommandLineParser parser) throws Exception {
        // Get and validate arguments
        ProcessArguments args = parseAndValidateArguments(parser);
        
        // Process files
        ProcessPoolResponse response = processFiles(args);
        
        // Display results
        displayResults(response, args.verbose);
    }
    
    @Override
    public boolean validate(CommandLineParser parser) {
        List<String> files = parser.getArguments();
        if (files.isEmpty()) {
            System.err.println(Messages.ERROR_PREFIX + Messages.AT_LEAST_ONE_FILE_REQUIRED);
            printHelp();
            return false;
        }
        return true;
    }
    
    private ProcessArguments parseAndValidateArguments(CommandLineParser parser) {
        ProcessArguments args = new ProcessArguments();
        
        // Get authentication token
        args.token = validateAndGetToken(parser);
        
        // Get required parameters
        String typeStr = getRequiredOption(parser, "tipo", "tp", Messages.DOCUMENT_TYPE_REQUIRED);
        String categoryStr = getRequiredOption(parser, "categoria", "c", Messages.VEHICLE_CATEGORY_REQUIRED);
        
        // Validate document type and category
        args.documentType = ValidationUtils.validateDocumentType(typeStr);
        args.vehicleCategory = ValidationUtils.validateVehicleCategory(categoryStr);
        
        // Get optional parameters
        args.name = parser.getOption("nombre", parser.getOption("n"));
        args.extractAccuracy = parser.hasFlag("precision") || parser.hasFlag("p");
        args.verbose = parser.hasFlag("verbose") || parser.hasFlag("v");
        
        // Handle process ID
        String processIdStr = parser.getOption("id", parser.getOption("i"));
        if (processIdStr != null) {
            args.processId = ValidationUtils.validateUUID(processIdStr);
        } else {
            args.processId = UUID.randomUUID();
        }
        
        // Validate files
        List<String> files = parser.getArguments();
        FileValidator.ValidationResult validationResult = fileValidator.validateFiles(files);
        
        if (!validationResult.hasValidFiles()) {
            throw new IllegalArgumentException(Messages.NO_VALID_FILES);
        }
        
        if (validationResult.hasErrors()) {
            System.err.println(Messages.WARNINGS_PREFIX + String.join(", ", validationResult.getErrors()));
        }
        
        args.validFiles = validationResult.getValidFiles();
        
        return args;
    }
    
    private ProcessPoolResponse processFiles(ProcessArguments args) throws MintApiException, IOException {
        if (args.verbose) {
            printProcessingInfo(args);
        }
        
        if (args.validFiles.size() == 1) {
            return processService.processSingleImage(
                args.token,
                args.validFiles.get(0),
                args.documentType,
                args.vehicleCategory,
                args.name,
                args.extractAccuracy
            );
        } else {
            return processService.processMultipleImages(
                args.token,
                args.validFiles,
                args.documentType,
                args.vehicleCategory,
                args.name,
                args.extractAccuracy
            );
        }
    }
    
    private void printProcessingInfo(ProcessArguments args) {
        System.out.printf(Messages.PROCESSING_FILES + "%n", args.validFiles.size());
        System.out.printf(Messages.PROCESS_ID + "%n", args.processId);
        System.out.printf(Messages.DOCUMENT_TYPE + "%n", args.documentType.getValue());
        System.out.printf(Messages.VEHICLE_CATEGORY + "%n", args.vehicleCategory.name());
        if (args.name != null) {
            System.out.printf(Messages.NAME + "%n", args.name);
        }
    }
    
    private void displayResults(ProcessPoolResponse response, boolean verbose) throws IOException {
        if (response.isSuccess()) {
            System.out.println();
            System.out.println(Constants.SUCCESS_SYMBOL + " " + Messages.PROCESSING_STARTED_SUCCESSFULLY);
            System.out.printf(Messages.PROCESS_ID + "%n", response.getId());
            System.out.println();
            System.out.println(Messages.CHECK_STATUS_WITH);
            System.out.println("  mintitv-cli retrieve " + response.getId());
        } else {
            System.out.println();
            System.out.println(Constants.ERROR_SYMBOL + " " + Messages.PROCESSING_ERROR);
            if (verbose) {
                System.out.println(objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(response));
            }
        }
    }
    
    @Override
    public void printHelp() {
        System.out.println(Messages.USAGE_PREFIX + COMMAND_USAGE);
        System.out.println();
        System.out.println(Messages.PROCESS_DESCRIPTION);
        System.out.println();
        System.out.println(Messages.ARGUMENTS_SECTION);
        System.out.println("  " + Messages.FILES_TO_PROCESS);
        System.out.println();
        System.out.println(Messages.REQUIRED_OPTIONS_SECTION);
        printOption("--tipo", "TIPO", Messages.OPTION_DOCUMENT_TYPE);
        printOption("-c", "--categoria CAT", Messages.OPTION_VEHICLE_CATEGORY);
        System.out.println();
        System.out.println(Messages.ADDITIONAL_OPTIONS_SECTION);
        printOption("-t", "--token TOKEN", Messages.OPTION_TOKEN);
        printOption("-n", "--nombre NOMBRE", Messages.OPTION_NAME);
        printOption("-i", "--id UUID", Messages.OPTION_ID);
        printOption("-p", "--precision", Messages.OPTION_EXTRACT_ACCURACY);
        printOption("-v", "--verbose", Messages.OPTION_VERBOSE);
        printOption("-h", "--help", Messages.OPTION_HELP);
        System.out.println();
        System.out.println(Messages.SUPPORTED_DOCUMENT_TYPES);
        System.out.println("  " + String.join(", ", Constants.SUPPORTED_DOCUMENT_TYPES));
        System.out.println();
        System.out.println(Messages.SUPPORTED_VEHICLE_CATEGORIES);
        System.out.println("  " + String.join(", ", Constants.SUPPORTED_VEHICLE_CATEGORIES));
        System.out.println();
        System.out.println(Messages.SUPPORTED_FILE_FORMATS);
        System.out.println("  JPEG (.jpg, .jpeg), PNG (.png), PDF (.pdf), TIFF (.tif, .tiff)");
        System.out.println();
        System.out.println(Messages.EXAMPLES_SECTION);
        System.out.println("  export MINTITV_TOKEN=\"your-token-here\"");
        System.out.println("  mintitv-cli process --tipo coc --categoria M1 document.pdf");
        System.out.println("  mintitv-cli process --tipo titv-new --categoria N1 front.jpg back.jpg");
        System.out.println("  mintitv-cli process --tipo coc --categoria M1 --nombre \"BMW 2024\" doc.pdf");
    }
    
    /**
     * Internal class to hold parsed and validated arguments.
     */
    private static class ProcessArguments {
        String token;
        DocumentType documentType;
        VehicleCategory vehicleCategory;
        String name;
        boolean extractAccuracy;
        boolean verbose;
        UUID processId;
        List<String> validFiles;
    }
}