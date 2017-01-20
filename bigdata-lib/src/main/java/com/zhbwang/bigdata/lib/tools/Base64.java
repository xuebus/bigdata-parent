// ----------------------------------------------------------------------------
// Copyright 2007-2012, GeoTelematic Solutions, Inc.
// All rights reserved
// ----------------------------------------------------------------------------
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
// http://www.apache.org/licenses/LICENSE-2.0
// 
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
// ----------------------------------------------------------------------------
// Description:
//  Base64 encoding/decoding
// ----------------------------------------------------------------------------
// Change History:
//  2006/03/26  Martin D. Flynn
//     -Initial release
//  2006/06/30  Martin D. Flynn
//     -Repackaged
//  2007/09/16  Martin D. Flynn
//     -Changed to allow overriding the alphabet used to encode bytes.
//  2009/01/28  Martin D. Flynn
//     -Added command-line encode/decode options.
//  2012/04/20  Martin D. Flynn
//     -Added "Base64HttpAlpha" Http-URL save alphabet.
//     -Added Alphabet option to shuffle.
// ----------------------------------------------------------------------------
package com.zhbwang.bigdata.lib.tools;


import java.math.BigInteger;

/**
 *** Base64 encoding/decoding tools
 **/

public class Base64 {

	// ------------------------------------------------------------------------

	/**
	 *** The Base64 padding character
	 **/
	public static final char Base64Pad = '=';

	/**
	 *** The Standard Base64 alphabet
	 **/
	public static final char Base64Alphabet[] = { 'A', 'B', 'C', 'D', 'E', 'F',
			'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S',
			'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f',
			'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
			't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', '+', '/', };

	/**
	 *** An HTTP-Safe Base64 alphabet
	 **/
	public static final char Base64HttpAlpha[] = { 'A', 'B', 'C', 'D', 'E',
			'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
			'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e',
			'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
			's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4',
			'5', '6', '7', '8', '9', '-', '_', };

	// ------------------------------------------------------------------------
	// ------------------------------------------------------------------------

	/**
	 *** Base64 Decode Parse Exception
	 **/
	public static class Base64DecodeException extends Exception {
		/**
		 * 
		 */
		private static final long serialVersionUID = -8940112583412128141L;

		public Base64DecodeException(String msg) {
			super(msg);
		}
	}

	// ------------------------------------------------------------------------
	// ------------------------------------------------------------------------

	/**
	 *** return a shuffled Base64 alphabet
	 *** 
	 * @param seed
	 *            A large number used as a randomizer seed to shuffle the
	 *            alphabet
	 *** @return The shuffled alphabet
	 **/
	public static char[] shuffleAlphabet(long seed) {
		return Base64.shuffleAlphabet(seed, Base64Alphabet);
	}

	/**
	 *** return a shuffled Base64 alphabet
	 *** 
	 * @param seed
	 *            A large number used as a randomizer seed to shuffle the
	 *            alphabet
	 *** @param alpha
	 *            Alphabet to shuffle
	 *** @return The shuffled alphabet
	 **/
	public static char[] shuffleAlphabet(long seed, char alpha[]) {
		char newAlpha[] = new char[Base64Alphabet.length];
		System.arraycopy(alpha, 0, newAlpha, 0, newAlpha.length);
		return ListTools.shuffle(newAlpha, seed);
	}

	// ------------------------------------------------------------------------

	/**
	 *** return a shuffled Base64 alphabet
	 *** 
	 * @param seed
	 *            A large number used as a randomizer seed to shuffle the
	 *            alphabet
	 *** @return The shuffled alphabet
	 **/
	public static char[] shuffleAlphabet(BigInteger seed) {
		return Base64.shuffleAlphabet(seed, Base64Alphabet);
	}

	/**
	 *** return a shuffled Base64 alphabet
	 *** 
	 * @param seed
	 *            A large number used as a randomizer seed to shuffle the
	 *            alphabet
	 *** @param alpha
	 *            Alphabet to shuffle
	 *** @return The shuffled alphabet
	 **/
	public static char[] shuffleAlphabet(BigInteger seed, char alpha[]) {
		char newAlpha[] = new char[Base64Alphabet.length];
		System.arraycopy(alpha, 0, newAlpha, 0, newAlpha.length);
		return ListTools.shuffle(newAlpha, seed);
	}

	// ------------------------------------------------------------------------
	// ------------------------------------------------------------------------

	/**
	 *** Returns a character from the specified alphabet based on the specified
	 * index
	 *** 
	 * @param ndx
	 *            The character array index
	 *** @param alpha
	 *            The character array alphabet
	 *** @return The character at the specified index
	 **/
	protected static char _encodeChar(int ndx, char alpha[]) {
		return alpha[ndx];
	}

	/**
	 *** Encodes the specified String based on the Base64 alphabet
	 *** 
	 * @param str
	 *            The String to encode
	 *** @return The Base64 encoded String
	 **/
	public static String encode(String str) {
		return (str != null) ? Base64.encode(str.getBytes()) : "";
	}

	/**
	 *** Encodes the specified byte array based on the Base64 alphabet
	 *** 
	 * @param buff
	 *            The byte array to encode
	 *** @return The Base64 encoded byte array
	 **/
	public static String encode(byte buff[]) {
		return Base64.encode(buff, Base64Alphabet, Base64Pad);
	}

	/**
	 *** Encodes the specified byte array based on the specified alphabet
	 *** 
	 * @param buff
	 *            The byte array to encode
	 *** @param alpha
	 *            The alphabet used to encode the byte array
	 *** @return The encoded byte array
	 **/
	public static String encode(byte buff[], char alpha[]) {
		return Base64.encode(buff, alpha, Base64Pad);
	}

	/**
	 *** Encodes the specified byte array based on the specified alphabet
	 *** 
	 * @param buff
	 *            The byte array to encode
	 *** @param alpha
	 *            The alphabet used to encode the byte array
	 *** @param pad
	 *            The padding character
	 *** @return The encoded byte array
	 **/
	public static String encode(byte buff[], char alpha[], char pad) {

		/* default alphabet */
		if (alpha == null) {
			alpha = Base64Alphabet;
		}

		/* encoded string buffer */
		StringBuffer sb = new StringBuffer();
		int len = buff.length;

		/* encode byte array */
		for (int i = 0; i < len; i += 3) {
			// 00000000 00000000 00000000
			// 10000010 00001000 00100000

			/* place next 3 bytes into register */
			int reg24 = ((int) buff[i] << 16) & 0xFF0000;
			if ((i + 1) < len) {
				reg24 |= ((int) buff[i + 1] << 8) & 0x00FF00;
			}
			if ((i + 2) < len) {
				reg24 |= ((int) buff[i + 2]) & 0x0000FF;
			}

			/* encode data 6 bits at a time */
			sb.append(alpha[(reg24 >>> 18) & 0x3F]);
			sb.append(alpha[(reg24 >>> 12) & 0x3F]);
			sb.append(((i + 1) < len) ? alpha[(reg24 >>> 6) & 0x3F] : pad);
			sb.append(((i + 2) < len) ? alpha[(reg24) & 0x3F] : pad);

		}

		/* return encoded string */
		return sb.toString();

	}

	/**
	 *** Encodes the specified byte array based on a shuffled Base64 alphabet
	 *** 
	 * @param buff
	 *            The byte array to encode
	 *** @param seed
	 *            A large number used as a randomizer seed to shuffle the
	 *            alphabet
	 *** @return The encoded byte array
	 **/
	public static String encode(byte buff[], long seed) {
		char alpha[] = Base64.shuffleAlphabet(seed);
		return Base64.encode(buff, alpha, Base64Pad);
	}

	/**
	 *** Encodes the specified byte array based on a shuffled Base64 alphabet
	 *** 
	 * @param buff
	 *            The byte array to encode
	 *** @param seed
	 *            A large number used as a randomizer seed to shuffle the
	 *            alphabet
	 *** @return The encoded byte array
	 **/
	public static String encode(byte buff[], BigInteger seed) {
		char alpha[] = Base64.shuffleAlphabet(seed);
		return Base64.encode(buff, alpha, Base64Pad);
	}

	// ------------------------------------------------------------------------
	// ------------------------------------------------------------------------

	/**
	 *** Returns the index of the specified character in the specified array
	 *** 
	 * @param ch
	 *            The target character for which the index is returned
	 *** @param alpha
	 *            The character alphabet
	 *** @return The index of the target character in the specified alphabet
	 **/
	protected static int _decodeChar(char ch, char alpha[])
			throws Base64DecodeException {
		for (int i = 0; i < alpha.length; i++) {
			if (ch == alpha[i]) {
				return i;
			}
		}
		// invalid character found
		throw new Base64DecodeException("Invalid char: " + ch);
	}

	/**
	 *** Decodes the specified String based on the Base64 alphabet
	 *** 
	 * @param b64Str
	 *            The String to decode
	 *** @return The decoded byte array
	 **/
	public static byte[] decode(String b64Str) throws Base64DecodeException {
		return Base64.decode(b64Str, Base64Alphabet, Base64Pad);
	}

	/**
	 *** Decodes the specified String based on the specified alphabet
	 *** 
	 * @param b64Str
	 *            The String to decode
	 *** @param alpha
	 *            The character alphabet used to decode the String
	 *** @return The decoded byte array
	 **/
	public static byte[] decode(String b64Str, char alpha[])
			throws Base64DecodeException {
		return Base64.decode(b64Str, alpha, Base64Pad);
	}

	/**
	 *** Decodes the specified String based on the specified alphabet
	 *** 
	 * @param b64Str
	 *            The String to decode
	 *** @param alpha
	 *            The character alphabet used to decode the String
	 *** @param pad
	 *            The padding character
	 *** @return The decoded byte array
	 **/
	public static byte[] decode(String b64Str, char alpha[], char pad)
			throws Base64DecodeException {

		/* default alphabet */
		if (alpha == null) {
			alpha = Base64Alphabet;
		}

		/* validate Base64 String */
		if ((b64Str == null) || b64Str.equals("")) {
			return new byte[0];
		}

		/* encoded string buffer - stip pad */
		int len = b64Str.length();
		while ((len > 0) && (b64Str.charAt(len - 1) == pad)) {
			len--;
		}

		/* output buffer length */
		// XX==, XXX=, XXXX, XXXXXX==
		int b = 0, blen = (((len - 1) / 4) * 3) + ((len - 1) % 4);
		if (((len - 1) % 4) == 0) {
			// the encoded Base64 String has an invalid length
			blen++; // allow for an extra byte
		}
		byte buff[] = new byte[blen];
		// 1=?0, 2=1, 3=2, 4=3, 5=?3, 6=4, 7=5, 8=6, 9=?6, 10=7, ...

		for (int i = 0; i < len; i += 4) {

			/* place next 4 characters into a 24-bit register */
			int reg24 = (_decodeChar(b64Str.charAt(i), alpha) << 18) & 0xFC0000;
			if ((i + 1) < len) {
				reg24 |= (_decodeChar(b64Str.charAt(i + 1), alpha) << 12) & 0x03F000;
			}
			if ((i + 2) < len) {
				reg24 |= (_decodeChar(b64Str.charAt(i + 2), alpha) << 6) & 0x000FC0;
			}
			if ((i + 3) < len) {
				reg24 |= (_decodeChar(b64Str.charAt(i + 3), alpha)) & 0x00003F;
			}

			/* decode register into 3 bytes */
			buff[b++] = (byte) ((reg24 >>> 16) & 0xFF);
			if ((i + 2) < len) {
				buff[b++] = (byte) ((reg24 >>> 8) & 0xFF);
			}
			if ((i + 3) < len) {
				buff[b++] = (byte) ((reg24) & 0xFF);
			}

		}

		return buff;

	}

	/**
	 *** Decodes the specified String based on a scambled Base64 alphabet
	 *** 
	 * @param b64Str
	 *            The String to decode
	 *** @param seed
	 *            A large number used as a randomizer seed to shuffle the
	 *            alphabet
	 *** @return The decoded byte array
	 **/
	public static byte[] decode(String b64Str, long seed)
			throws Base64DecodeException {
		char alpha[] = Base64.shuffleAlphabet(seed);
		return Base64.decode(b64Str, alpha, Base64Pad);
	}

	/**
	 *** Decodes the specified String based on a scambled Base64 alphabet
	 *** 
	 * @param b64Str
	 *            The String to decode
	 *** @param seed
	 *            A large number used as a randomizer seed to shuffle the
	 *            alphabet
	 *** @return The decoded byte array
	 **/
	public static byte[] decode(String b64Str, BigInteger seed)
			throws Base64DecodeException {
		char alpha[] = Base64.shuffleAlphabet(seed);
		return Base64.decode(b64Str, alpha, Base64Pad);
	}

	// ------------------------------------------------------------------------
	// ------------------------------------------------------------------------
	// ------------------------------------------------------------------------

	private static final String ARG_DECODE[] = new String[] { "decode", "d" };
	private static final String ARG_ENCODE[] = new String[] { "encode", "e" };
	private static final String ARG_SEED[] = new String[] { "seed", "s" };

	private static void usage() {
		System.out.println("Usage:");
		System.out.println("  java ... " + Base64.class.getName() + " {options}");
		System.out.println("Options:");
		System.out.println("  -decode=<Base64>   Decode Base64 string to ASCII");
		System.out.println("  -encode=<ASCII>    Encode ASCII string to Base64");
		System.exit(1);
	}

	/**
	 *** Main entry point for testing/debugging
	 *** 
	 * @param argv
	 *            Comand-line arguments
	 **/
	public static void main(String argv[]) throws Base64DecodeException {
//		RTProperties props = RTProperties.parseCommandLineArgs(argv);
//		long seed = props.getLong(ARG_SEED, 0L);
//
//		/* decode Base64 strings */
//		if (props.hasProperty(ARG_DECODE)) {
//			String b64 = props.getString(ARG_DECODE, "");
//			try {
//				byte b[] = (seed > 0L) ? Base64.decode(b64, seed) : Base64
//						.decode(b64);
//				System.out.println("Hex  : 0x" + StringTools.toHexString(b));
//				System.out.println("ASCII: " + StringTools.toStringValue(b, '.'));
//				System.exit(0);
//			} catch (Base64DecodeException bde) {
//				System.err.println("Invalid data" + bde);
//				System.exit(1);
//			}
//		}
//
//		/* encode Base64 strings */
//		if (props.hasProperty(ARG_ENCODE)) {
//			String ascii = props.getString(ARG_ENCODE, "");
//			byte b[] = ascii.startsWith("0x") ? StringTools.parseHex(ascii,
//					new byte[0]) : ascii.getBytes();
//			String b64 = (seed > 0L) ? Base64.encode(b, seed) : Base64
//					.encode(b);
//			System.out.println("Base64: " + b64);
//			System.exit(0);
//		}
//
//		/* no options */
//		usage();


		String tmp = "PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0idXRmLTgiPz48c3M6V29ya2Jvb2sgeG1sbnM6c3M9InVybjpzY2hlbWFzLW1pY3Jvc29mdC1jb206b2ZmaWNlOnNwcmVhZHNoZWV0IiB4bWxuczp4PSJ1cm46c2NoZW1hcy1taWNyb3NvZnQtY29tOm9mZmljZTpleGNlbCIgeG1sbnM6bz0idXJuOnNjaGVtYXMtbWljcm9zb2Z0LWNvbTpvZmZpY2U6b2ZmaWNlIj48bzpEb2N1bWVudFByb3BlcnRpZXM+PG86VGl0bGU+ZOaxieWtlzwvbzpUaXRsZT48L286RG9jdW1lbnRQcm9wZXJ0aWVzPjxzczpFeGNlbFdvcmtib29rPjxzczpXaW5kb3dIZWlnaHQ+OTAwMDwvc3M6V2luZG93SGVpZ2h0PjxzczpXaW5kb3dXaWR0aD4zMjEyMDwvc3M6V2luZG93V2lkdGg+PHNzOlByb3RlY3RTdHJ1Y3R1cmU+RmFsc2U8L3NzOlByb3RlY3RTdHJ1Y3R1cmU+PHNzOlByb3RlY3RXaW5kb3dzPkZhbHNlPC9zczpQcm90ZWN0V2luZG93cz48L3NzOkV4Y2VsV29ya2Jvb2s+PHNzOlN0eWxlcz48c3M6U3R5bGUgc3M6SUQ9IkRlZmF1bHQiPjxzczpBbGlnbm1lbnQgc3M6VmVydGljYWw9IlRvcCIgIC8+PHNzOkZvbnQgc3M6Rm9udE5hbWU9ImFyaWFsIiBzczpTaXplPSIxMCIgLz48c3M6Qm9yZGVycz48c3M6Qm9yZGVyICBzczpXZWlnaHQ9IjEiIHNzOkxpbmVTdHlsZT0iQ29udGludW91cyIgc3M6UG9zaXRpb249IlRvcCIgLz48c3M6Qm9yZGVyICBzczpXZWlnaHQ9IjEiIHNzOkxpbmVTdHlsZT0iQ29udGludW91cyIgc3M6UG9zaXRpb249IkJvdHRvbSIgLz48c3M6Qm9yZGVyICBzczpXZWlnaHQ9IjEiIHNzOkxpbmVTdHlsZT0iQ29udGludW91cyIgc3M6UG9zaXRpb249IkxlZnQiIC8+PHNzOkJvcmRlciBzczpXZWlnaHQ9IjEiIHNzOkxpbmVTdHlsZT0iQ29udGludW91cyIgc3M6UG9zaXRpb249IlJpZ2h0IiAvPjwvc3M6Qm9yZGVycz48c3M6SW50ZXJpb3IgLz48c3M6TnVtYmVyRm9ybWF0IC8+PHNzOlByb3RlY3Rpb24gLz48L3NzOlN0eWxlPjxzczpTdHlsZSBzczpJRD0idGl0bGUiPjxzczpCb3JkZXJzIC8+PHNzOkZvbnQgLz48c3M6QWxpZ25tZW50ICBzczpWZXJ0aWNhbD0iQ2VudGVyIiBzczpIb3Jpem9udGFsPSJDZW50ZXIiIC8+PHNzOk51bWJlckZvcm1hdCBzczpGb3JtYXQ9IkAiIC8+PC9zczpTdHlsZT48c3M6U3R5bGUgc3M6SUQ9ImhlYWRlcmNlbGwiPjxzczpGb250IHNzOkJvbGQ9IjEiIHNzOlNpemU9IjEwIiAvPjxzczpBbGlnbm1lbnQgIHNzOkhvcml6b250YWw9IkNlbnRlciIgLz48c3M6SW50ZXJpb3Igc3M6UGF0dGVybj0iU29saWQiICAvPjwvc3M6U3R5bGU+PHNzOlN0eWxlIHNzOklEPSJldmVuIj48c3M6SW50ZXJpb3Igc3M6UGF0dGVybj0iU29saWQiICAvPjwvc3M6U3R5bGU+PHNzOlN0eWxlIHNzOlBhcmVudD0iZXZlbiIgc3M6SUQ9ImV2ZW5kYXRlIj48c3M6TnVtYmVyRm9ybWF0IHNzOkZvcm1hdD0ieXl5eS1tbS1kZCIgLz48L3NzOlN0eWxlPjxzczpTdHlsZSBzczpQYXJlbnQ9ImV2ZW4iIHNzOklEPSJldmVuaW50Ij48c3M6TnVtYmVyRm9ybWF0IHNzOkZvcm1hdD0iMCIgLz48L3NzOlN0eWxlPjxzczpTdHlsZSBzczpQYXJlbnQ9ImV2ZW4iIHNzOklEPSJldmVuZmxvYXQiPjxzczpOdW1iZXJGb3JtYXQgc3M6Rm9ybWF0PSIwLjAwIiAvPjwvc3M6U3R5bGU+PHNzOlN0eWxlIHNzOklEPSJvZGQiPjxzczpJbnRlcmlvciBzczpQYXR0ZXJuPSJTb2xpZCIgIC8+PC9zczpTdHlsZT48c3M6U3R5bGUgc3M6UGFyZW50PSJvZGQiIHNzOklEPSJvZGRkYXRlIj48c3M6TnVtYmVyRm9ybWF0IHNzOkZvcm1hdD0ieXl5eS1tbS1kZCIgLz48L3NzOlN0eWxlPjxzczpTdHlsZSBzczpQYXJlbnQ9Im9kZCIgc3M6SUQ9Im9kZGludCI+PHNzOk51bWJlckZvcm1hdCBzczpGb3JtYXQ9IjAiIC8+PC9zczpTdHlsZT48c3M6U3R5bGUgc3M6UGFyZW50PSJvZGQiIHNzOklEPSJvZGRmbG9hdCI+PHNzOk51bWJlckZvcm1hdCBzczpGb3JtYXQ9IjAuMDAiIC8+PC9zczpTdHlsZT48L3NzOlN0eWxlcz48c3M6V29ya3NoZWV0IHNzOk5hbWU9ImTmsYnlrZciPjxzczpOYW1lcz48c3M6TmFtZWRSYW5nZSBzczpOYW1lPSJQcmludF9UaXRsZXMiIHNzOlJlZmVyc1RvPSI9J2TmsYnlrZcnIVIxOlIyIiAvPjwvc3M6TmFtZXM+PHNzOlRhYmxlIHg6RnVsbFJvd3M9IjEiIHg6RnVsbENvbHVtbnM9IjEiIHNzOkV4cGFuZGVkQ29sdW1uQ291bnQ9IjciIHNzOkV4cGFuZGVkUm93Q291bnQ9IjMiPjxzczpDb2x1bW4gc3M6QXV0b0ZpdFdpZHRoPSIxIiBzczpXaWR0aD0iMTMwIiAvPjxzczpDb2x1bW4gc3M6QXV0b0ZpdFdpZHRoPSIxIiBzczpXaWR0aD0iMTMwIiAvPjxzczpDb2x1bW4gc3M6QXV0b0ZpdFdpZHRoPSIxIiBzczpXaWR0aD0iMTMwIiAvPjxzczpDb2x1bW4gc3M6QXV0b0ZpdFdpZHRoPSIxIiBzczpXaWR0aD0iMTMwIiAvPjxzczpDb2x1bW4gc3M6QXV0b0ZpdFdpZHRoPSIxIiBzczpXaWR0aD0iMTMwIiAvPjxzczpSb3cgc3M6QXV0b0ZpdEhlaWdodD0iMSI+PHNzOkNlbGwgc3M6U3R5bGVJRD0iaGVhZGVyY2VsbCI+PHNzOkRhdGEgc3M6VHlwZT0iU3RyaW5nIj5Qcm9kdWN0IElEPC9zczpEYXRhPjxzczpOYW1lZENlbGwgc3M6TmFtZT0iUHJpbnRfVGl0bGVzIiAvPjwvc3M6Q2VsbD48c3M6Q2VsbCBzczpTdHlsZUlEPSJoZWFkZXJjZWxsIj48c3M6RGF0YSBzczpUeXBlPSJTdHJpbmciPkxpc3QgUHJpY2U8L3NzOkRhdGE+PHNzOk5hbWVkQ2VsbCBzczpOYW1lPSJQcmludF9UaXRsZXMiIC8+PC9zczpDZWxsPjxzczpDZWxsIHNzOlN0eWxlSUQ9ImhlYWRlcmNlbGwiPjxzczpEYXRhIHNzOlR5cGU9IlN0cmluZyI+VW5pdCBDb3N0PC9zczpEYXRhPjxzczpOYW1lZENlbGwgc3M6TmFtZT0iUHJpbnRfVGl0bGVzIiAvPjwvc3M6Q2VsbD48c3M6Q2VsbCBzczpTdHlsZUlEPSJoZWFkZXJjZWxsIj48c3M6RGF0YSBzczpUeXBlPSJTdHJpbmciPkF0dHJpYnV0ZTwvc3M6RGF0YT48c3M6TmFtZWRDZWxsIHNzOk5hbWU9IlByaW50X1RpdGxlcyIgLz48L3NzOkNlbGw+PHNzOkNlbGwgc3M6U3R5bGVJRD0iaGVhZGVyY2VsbCI+PHNzOkRhdGEgc3M6VHlwZT0iU3RyaW5nIj5TdGF1dHM8L3NzOkRhdGE+PHNzOk5hbWVkQ2VsbCBzczpOYW1lPSJQcmludF9UaXRsZXMiIC8+PC9zczpDZWxsPjwvc3M6Um93PjxzczpSb3c+PHNzOkNlbGwgc3M6U3R5bGVJRD0iZXZlbiI+PHNzOkRhdGEgc3M6VHlwZT0iU3RyaW5nIj4xMjA0NDk8L3NzOkRhdGE+PC9zczpDZWxsPjxzczpDZWxsIHNzOlN0eWxlSUQ9ImV2ZW4iPjxzczpEYXRhIHNzOlR5cGU9IlN0cmluZyI+5bedQTQyUzI5PC9zczpEYXRhPjwvc3M6Q2VsbD48c3M6Q2VsbCBzczpTdHlsZUlEPSJldmVuIj48c3M6RGF0YSBzczpUeXBlPSJTdHJpbmciPjAyOC0wMTA4PC9zczpEYXRhPjwvc3M6Q2VsbD48c3M6Q2VsbCBzczpTdHlsZUlEPSJldmVuIj48c3M6RGF0YSBzczpUeXBlPSJTdHJpbmciPnVuZGVmaW5lZDwvc3M6RGF0YT48L3NzOkNlbGw+PHNzOkNlbGwgc3M6U3R5bGVJRD0iZXZlbiI+PHNzOkRhdGEgc3M6VHlwZT0iU3RyaW5nIj51bmRlZmluZWQ8L3NzOkRhdGE+PC9zczpDZWxsPjwvc3M6Um93Pjwvc3M6VGFibGU+PHg6V29ya3NoZWV0T3B0aW9ucz48eDpQYWdlU2V0dXA+PHg6TGF5b3V0IHg6Q2VudGVySG9yaXpvbnRhbD0iMSIgeDpPcmllbnRhdGlvbj0iTGFuZHNjYXBlIiAvPjx4OkZvb3RlciB4OkRhdGE9IlBhZ2UgJmFtcDtQIG9mICZhbXA7TiIgeDpNYXJnaW49IjAuNSIgLz48eDpQYWdlTWFyZ2lucyB4OlRvcD0iMC41IiB4OlJpZ2h0PSIwLjUiIHg6TGVmdD0iMC41IiB4OkJvdHRvbT0iMC44IiAvPjwveDpQYWdlU2V0dXA+PHg6Rml0VG9QYWdlIC8+PHg6UHJpbnQ+PHg6UHJpbnRFcnJvcnM+Qmxhbms8L3g6UHJpbnRFcnJvcnM+PHg6Rml0V2lkdGg+MTwveDpGaXRXaWR0aD48eDpGaXRIZWlnaHQ+MzI3Njc8L3g6Rml0SGVpZ2h0Pjx4OlZhbGlkUHJpbnRlckluZm8gLz48eDpWZXJ0aWNhbFJlc29sdXRpb24+NjAwPC94OlZlcnRpY2FsUmVzb2x1dGlvbj48L3g6UHJpbnQ+PHg6U2VsZWN0ZWQgLz48eDpEb05vdERpc3BsYXlHcmlkbGluZXMgLz48eDpQcm90ZWN0T2JqZWN0cz5GYWxzZTwveDpQcm90ZWN0T2JqZWN0cz48eDpQcm90ZWN0U2NlbmFyaW9zPkZhbHNlPC94OlByb3RlY3RTY2VuYXJpb3M+PC94OldvcmtzaGVldE9wdGlvbnM+PC9zczpXb3Jrc2hlZXQ+PC9zczpXb3JrYm9vaz4=";
		String decode = StringTools.toStringValue(Base64.decode(tmp));
		System.out.println(decode);

	}

}
