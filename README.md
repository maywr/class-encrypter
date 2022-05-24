<div align="center">
    
### class encrypter

programm that crypts ur jar classes

</div>

#### info
this is not an "obfuscator" and you will not be able to use
a jar after crypt because you firstly need to decrypt it 
by using the following code:

<strong>
YOU ALSO WILL NEED A KEY WITH WHICH CLASSES WERE ENCRYPTED
</strong>


```java
public static byte[] decrypt(final byte[] input, final String secret) {
	try {
		setKey(secret);
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
		cipher.init(Cipher.DECRYPT_MODE, secretKey);
		return cipher.doFinal(input);
	} catch (Exception e) {
		System.out.println("Error while decrypting: " + e.toString());
	}
	return null;
}
```
(also do not forget about copying other methods from AES class like setKey)

#### how to!!
just run a jar by `java -jar` and you will get the example
config also an input arguments
<br><img src="https://i.imgur.com/56cyZ1h.png" alt="pic">

#### why
can be used in class loaders, plugin systems and many other
ways where you need to "inject" a jar in runtime!

#### libs
* apache commons io - reading input/output streams into bytes array
* org.json for config loading (i could use gson actually but idk meh)
* apache commons cli - reading console input arguments
