package se.smrt.generator.types;

public enum Wildcard {
	NONE {
		@Override
		public String getName(String name) {
			return name;
		}},

	SUPER {
		@Override
		public String getName(String name) {
			return "? super " + name;
		}},

	EXTENDS {
		@Override
		public String getName(String name) {
			return "? extends " + name;
		}
	},

	ANY {
		@Override
		public String getName(String name) {
			return "?";
		}
	};

	public abstract String getName(String name);

}
