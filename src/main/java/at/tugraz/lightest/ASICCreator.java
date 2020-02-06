package at.tugraz.lightest;

import eu.europa.esig.dss.Policy;
import org.digidoc4j.*;
import org.digidoc4j.exceptions.DigiDoc4JException;
import org.digidoc4j.signers.PKCS12SignatureToken;

import java.io.FileNotFoundException;
import java.security.cert.CertificateException;


public class ASICCreator {
    
    public ASICCreator() throws CertificateException, FileNotFoundException {
        Configuration.getInstance().setTspSource("http://demo.sk.ee/tsa"); // Test TSP for now ...
        //Configuration.getInstance().setTslLocation(trustList);
        Configuration.getInstance().setPrintValidationReport(true);
                
        //Configuration.getInstance().setTslKeyStoreLocation(KeystoreGenerator.DEFAULT_KEYSTORE_FILEPATH);
        //Configuration.getInstance().setTslKeyStorePassword(KeystoreGenerator.DEFAULT_KEYSTORE_PASSWORD);
    }
    
    /**
     * This function creates the necessary container
     *
     * @param pathToFile              file to sign
     * @param containerType           type of container to create
     * @param certificateFileName     .p12 file name and path
     * @param certificateFilePassword keystore password
     * @return
     */
    public Container createASic(String pathToFile, Container.DocumentType containerType, String certificateFileName, String certificateFilePassword) {
        return createASic(pathToFile, containerType, certificateFileName, certificateFilePassword, null);
    }
    
    /**
     * This function creates the necessary container
     *
     * @param pathToFile              file to sign
     * @param containerType           type of container to create
     * @param certificateFileName     .p12 file name and path
     * @param certificateFilePassword keystore password
     * @param alias                   known key alias in keystore
     * @return
     */
    public Container createASic(String pathToFile, Container.DocumentType containerType, String certificateFileName, String certificateFilePassword, String alias) {
        Container container = ContainerBuilder.
                aContainer(containerType).
                //withDataFile("sample_transaction/contract.txt", "text/plain"). // Edit this place to add different files to the container. YOu can add multiple of this function
                        withDataFile(pathToFile, "application/xml").
                        build();
        
        PKCS12SignatureToken signatureToken = new PKCS12SignatureToken(certificateFileName, certificateFilePassword, alias);
        
        System.out.println("Init done ...");
        System.out.println("Using key: " + signatureToken.getCertificate().getSubjectDN());
        
        createSignatureAndSign(container, signatureToken);
        return container;
    }
    
    /**
     * Sign the container
     *
     * @param container
     * @param signatureToken
     */
    public void createSignatureAndSign(Container container, PKCS12SignatureToken signatureToken) {
        Container container1 = container;
        PKCS12SignatureToken signToken = signatureToken;
                
        System.out.println("Lets do the actual signing ...");
        Policy mypolicy;
        
        Signature signature = SignatureBuilder.
                aSignature(container1).
                withSignatureDigestAlgorithm(DigestAlgorithm.SHA256).
                withSignatureToken(signToken).
                withSignatureProfile(SignatureProfile.B_BES).
                //withOwnSignaturePolicy(mypolicy).
                        invokeSigning();
        
        System.out.println("Signing done ...");
        
        assert signature != null;
        if(signature == null) {
            System.out.println("signature is null ...");
        }
        
        container1.addSignature(signature);
    }
    
    /**
     * Validate the container created
     *
     * @param container
     */
    public boolean validateASIC(Container container) {
        Container container2 = container;
        
        //container2.getConfiguration().setTslLocation(trustList);
        
        ValidationResult validationResult = container2.validate();
        
        System.out.println("Validation Report: ");
        System.out.println(((ContainerValidationResult) validationResult).getReport());
        
        System.out.println("Used validation policy: " + Configuration.getInstance().getValidationPolicy());
        
        if(!validationResult.isValid()) {
            for(DigiDoc4JException error : validationResult.getErrors()) {
                System.out.println(error.getMessage());
            }
            
            return false;
        }
        
        return true;
    }
}
