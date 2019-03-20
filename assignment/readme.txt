Compile:
-------- 
mvn clean install

Execute:
-------
java -jar target/assignment-1.0-SNAPSHOT.jar Apples Milk

 
Assumptions:
-----------

Items added by the user on the command-line are case-insensitive. ie. "Apple" and "apple" will resolve to the same item. This also means that the editor of product.xml must not specify items with the same name and conflicting case.

Attempts to add invalid Items to the Basket are ignored.

Expiry dates are inclusive. If the expiry date is today, the offer will still be applicable.