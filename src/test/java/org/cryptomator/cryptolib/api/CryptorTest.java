package org.cryptomator.cryptolib.api;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

public class CryptorTest {

	private final Cryptor cryptor = Mockito.mock(Cryptor.class);

	@BeforeEach
	public void setup() {
		FileContentCryptor contentCryptor = Mockito.mock(FileContentCryptor.class);
		Mockito.when(cryptor.fileContentCryptor()).thenReturn(contentCryptor);
		Mockito.when(contentCryptor.cleartextChunkSize()).thenReturn(32);
		Mockito.when(contentCryptor.ciphertextChunkSize()).thenReturn(40);
		Mockito.doCallRealMethod().when(cryptor).cleartextSize(Mockito.anyLong());
		Mockito.doCallRealMethod().when(cryptor).ciphertextSize(Mockito.anyLong());
	}

	@ParameterizedTest(name = "cleartextSize({1}) == {0}")
	@CsvSource(value = {
			"0,0",
			"1,9",
			"31,39",
			"32,40",
			"33,49",
			"34,50",
			"63,79",
			"64,80",
			"65,89"
	})
	public void testCleartextSize(int cleartextSize, int ciphertextSize) {
		Assertions.assertEquals(cleartextSize, cryptor.cleartextSize(ciphertextSize));
	}

	@ParameterizedTest(name = "cleartextSize({0}) == undefined")
	@ValueSource(ints = {-1, 1, 8, 41, 48, 81, 88})
	public void testCleartextSizeWithInvalidCiphertextSize(int invalidCiphertextSize) {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			cryptor.cleartextSize(invalidCiphertextSize);
		});
	}

	@ParameterizedTest(name = "ciphertextSize({0}) == {1}")
	@CsvSource(value = {
			"0,0",
			"1,9",
			"31,39",
			"32,40",
			"33,49",
			"34,50",
			"63,79",
			"64,80",
			"65,89"
	})
	public void testCiphertextSize(int cleartextSize, int ciphertextSize) {
		Assertions.assertEquals(ciphertextSize, cryptor.ciphertextSize(cleartextSize));
	}

	@ParameterizedTest(name = "ciphertextSize({0}) == undefined")
	@ValueSource(ints = {-1})
	public void testCiphertextSizewithInvalidCleartextSize(int invalidCleartextSize) {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			cryptor.ciphertextSize(invalidCleartextSize);
		});
	}

}