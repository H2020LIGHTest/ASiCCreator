NOTES
======

## Create a self-signed certificate
* openssl ecparam -genkey -name prime256v1 -out key.pem
* openssl req -new -sha256 -key key.pem -out csr.csr
* openssl req -x509 -sha256 -days 365 -key key.pem -in csr.csr -out certificate.pem
* openssl req -in csr.csr -text -noout | grep -i "Signature.*SHA256" && echo "All is well" || echo "This certificate will stop working in 2017! You must update OpenSSL to generate a widely-compatible certificate"

## Change it to a P12 Store
* openssl pkcs12 -export -out certificate.p12 -inkey key.pem -in certificate.pem
