rem Please make sure that all the java files and the following subdirectories
rem lies in the same directory leve
rem the /net package
rem /resources
rem /outputimages

javadoc -private -author -d documentation -use -splitIndex -windowtitle "Visual Cryptography Package v1.0" -doctitle "Visual Cryptography Package v1.0" -header "Visual Cryptography Package v1.0" -bottom "Submit a bug or feature at <a href='mailto:laizzez_faire07@yahoo.com'>laizzez_faire07(@)yahoo.com</a>. Visual Cryptography is an implementation of the newly discovered and developed visual encryption algorithm by Naor and Shamir at the Eurocrypt 1994. Copyright 2005 <a href='http://ics.uplb.edu.ph'> University of the Philippines, Los Baños, Laguna</a>. All rights reserved." -footer "Visual Cryptography Package v1.0" -group "Core Packages" "java.*:com.sun.java.*:org.omg.*:net.sourceforge.jiu.*" -group "Extension Packages" "javax.*" *.java