package demo;

public class Text {

	public static void main(String[] args) {
		YXMap< String, String> map = new YXHashMap<String ,String>();
		for (int i = 0; i < 60; i++) {
			map.put("key"+i, "value"+i);
		}
		for (int i = 0; i < map.size(); i++) {
			System.out.println("key"+i+"===="+map.get("key"+i));
		}
		System.out.println(map.size());
	}

}
