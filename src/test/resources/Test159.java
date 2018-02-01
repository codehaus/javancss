interface Interface1 {
	static void staticTest(String str) {
		System.out.println(str);
	}

	default int defaultTest() {
		return 1;
	}
}
