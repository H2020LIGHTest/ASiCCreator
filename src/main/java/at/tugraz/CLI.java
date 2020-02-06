package at.tugraz;

import at.tugraz.lightest.ASICCreator;
import org.apache.commons.cli.*;
import org.digidoc4j.Container;

public class CLI {
    
    private static CommandLine parseCLI(String[] args) {
        Options options = new Options();
    
        Option pathToCertOpt = new Option("c", "pathToCert", true, "path to pkcs12 certificate used for signing");
        pathToCertOpt.setRequired(true);
        options.addOption(pathToCertOpt);
    
        Option certPasswordOpt = new Option("p", "certPassword", true, "password for pkcs12 certificate used for signing");
        certPasswordOpt.setRequired(false);
        options.addOption(certPasswordOpt);
    
        Option pathToDocOpt = new Option("d", "pathToDoc", true, "path to transaction xml");
        pathToDocOpt.setRequired(true);
        options.addOption(pathToDocOpt);
    
        Option containerNameOpt = new Option("o", "containerName", true, "name of created container");
        containerNameOpt.setRequired(true);
        options.addOption(containerNameOpt);
    
    
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;
    
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("asiccreator", options);
        
            System.exit(1);
        }
        
        return cmd;
    }
    
    public static void main(String[] args) throws Exception {
    
        CommandLine cmd = parseCLI(args);
        
        String pathToCert = cmd.getOptionValue("pathToCert");
        String pathToDoc = cmd.getOptionValue("pathToDoc");
        String certPassword = cmd.getOptionValue("certPassword");
        String containerName = cmd.getOptionValue("containerName");
        
        System.out.println("cert:     " + pathToCert);
        System.out.println("password: " + certPassword);
        System.out.println("doc:      " + pathToDoc);
        System.out.println("output:   " + containerName);
    
        Container.DocumentType docType = Container.DocumentType.ASICE;
        ASICCreator creator = new ASICCreator();
        Container container = creator.createASic(pathToDoc, docType, pathToCert, certPassword);
        
    
        container.saveAsFile(containerName);
    }
}
