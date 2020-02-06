package at.tugraz;

import at.tugraz.lightest.ASICCreator;
import org.digidoc4j.Container;
import org.digidoc4j.impl.asic.xades.validation.ThreadPoolManager;

import java.io.FileNotFoundException;
import java.security.cert.CertificateException;
import java.util.concurrent.ExecutorService;

/**
 * ASiC creator
 */
public class App {
    
    public static void main(String[] args) throws CertificateException, FileNotFoundException {
        
        String pathToCert = "keys/certificate.p12";

        String certPassword = "asdf"; // pass for the p12 store
    
        String pathToDoc = "theAuctionHouse/bid.xml";
        
        Container.DocumentType docType = Container.DocumentType.ASICE; // type of ASiC container you need

        ASICCreator creator = new ASICCreator();
        
        Container container = creator.createASic(pathToDoc, docType, pathToCert, certPassword); // Create an ASiC container

        String containerName = "LIGHTest_theAuctionHouse";

        switch(docType) { // Create the correct extension
            case ASICS:
                containerName = containerName + ".asics";
                break;
            case ASICE:
                containerName = containerName + ".asice";
                break;
            default:

        }

        container.saveAsFile(containerName); // Save the created ASiC. Ensure that the extension is provided

        System.out.println("Saving done!");
    
//        ASICCreator createASIC = new ASICCreator();
//        Container container;
//        container = ContainerBuilder.aContainer().fromExistingFile("iaik-test-scheme/container-signed-using-dss.asice").build();
        
        creator.validateASIC(container); // This validates the container
    
    
        ExecutorService executor = ThreadPoolManager.getDefaultThreadExecutor();
        if(executor != null) {
            executor.shutdownNow();
        }
    }
}
