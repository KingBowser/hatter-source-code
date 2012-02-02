
AESCrypt Java
-------------

This package contains the Java class es.vocali.util.AESCrypt
which provides file encryption and decryption using
the aescrypt file format specified at:

   http://www.aescrypt.com/aes_file_format.html

Versions 1 & 2 are supported both for reading and writing.
Version 0 is not supported.

Requirements
------------

It only works in Java 6, but can be easily adapted to Java 5
by replacing the call to NetworkInterface.getHardwareAddress()
with something else.

In order to use 256 bit AES keys, you must download and install
"Java Cryptography Extension (JCE) Unlimited Strength Jurisdiction
Policy Files" from:

   http://java.sun.com/javase/downloads/index.jsp

How to use
----------

- Embedded in Java project:
  See JavaDoc and typical use in AESCrypt.main() method.
  There are no dependencies other than Java 6 API.

- From the command line:

  java -cp bin es.vocali.util.AESCrypt e|d password fromPath toPath

  Operation mode is selected with the (e)ncrypt/(d)ecrypt switch.
  
- Running the tests (Unix command line only):

  cd test
  ./test.sh

License
-------

Copyright 2008 Vócali Sistemas Inteligentes

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
