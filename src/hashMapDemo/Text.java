package hashMapDemo;

public class Text {
	public static void main(String[] args) {
		MapDemo< String, String> mapdemo = new HashMapDemo<String ,String>();
		for (int i = 0; i < 100; i++) {
			mapdemo.put("key"+i, "value "+i);
		}
		for (int i = 0; i <100; i++) {
			System.out.println("key"+i+"==="+mapdemo.get("key"+i));
		}
		System.out.println(mapdemo.size());
 	}
}
