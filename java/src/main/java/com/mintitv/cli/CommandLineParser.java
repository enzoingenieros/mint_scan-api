package com.mintitv.cli;

import java.util.*;

/**
 * Simple command line argument parser.
 */
public class CommandLineParser {
    
    private final Map<String, String> options = new HashMap<>();
    private final List<String> arguments = new ArrayList<>();
    private final Set<String> flags = new HashSet<>();
    
    /**
     * Parses command line arguments.
     * 
     * @param args the command line arguments
     */
    public CommandLineParser(String[] args) {
        parse(args);
    }
    
    private void parse(String[] args) {
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            
            if (arg.startsWith("--")) {
                // Long option
                String key = arg.substring(2);
                if (i + 1 < args.length && !args[i + 1].startsWith("-")) {
                    options.put(key, args[++i]);
                } else {
                    flags.add(key);
                }
            } else if (arg.startsWith("-") && arg.length() > 1) {
                // Short option
                String key = arg.substring(1);
                if (key.length() == 1 && i + 1 < args.length && !args[i + 1].startsWith("-")) {
                    options.put(key, args[++i]);
                } else {
                    // Multiple flags like -abc
                    for (char c : key.toCharArray()) {
                        flags.add(String.valueOf(c));
                    }
                }
            } else {
                // Regular argument
                arguments.add(arg);
            }
        }
    }
    
    /**
     * Gets an option value by key.
     * 
     * @param key the option key (without dashes)
     * @return the option value, or null if not present
     */
    public String getOption(String key) {
        return options.get(key);
    }
    
    /**
     * Gets an option value with a default.
     * 
     * @param key the option key
     * @param defaultValue the default value
     * @return the option value or default
     */
    public String getOption(String key, String defaultValue) {
        return options.getOrDefault(key, defaultValue);
    }
    
    /**
     * Checks if a flag is present.
     * 
     * @param flag the flag name (without dashes)
     * @return true if the flag is present
     */
    public boolean hasFlag(String flag) {
        return flags.contains(flag);
    }
    
    /**
     * Gets positional arguments.
     * 
     * @return list of positional arguments
     */
    public List<String> getArguments() {
        return new ArrayList<>(arguments);
    }
    
    /**
     * Gets a specific positional argument.
     * 
     * @param index the argument index
     * @return the argument value, or null if index is out of bounds
     */
    public String getArgument(int index) {
        return index < arguments.size() ? arguments.get(index) : null;
    }
    
    /**
     * Gets the number of positional arguments.
     * 
     * @return the number of arguments
     */
    public int getArgumentCount() {
        return arguments.size();
    }
    
    /**
     * Checks if help was requested.
     * 
     * @return true if help flag is present
     */
    public boolean isHelpRequested() {
        return hasFlag("help") || hasFlag("h");
    }
    
    /**
     * Gets required option or throws exception.
     * 
     * @param key the option key
     * @param errorMessage error message if option is missing
     * @return the option value
     * @throws IllegalArgumentException if option is missing
     */
    public String getRequiredOption(String key, String errorMessage) {
        String value = getOption(key);
        if (value == null) {
            throw new IllegalArgumentException(errorMessage);
        }
        return value;
    }
}