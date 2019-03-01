package demo;

import java.util.ArrayList;
import java.util.List;

public class YXHashMap<K, V> implements YXMap<K, V> {
	
	private static   int  defaultLength=16;
	
	private static  double defaultLoader=0.75;
	
	//底层的基础是数组
	
	private  Entry<K,V>[] table=null;
	
	//hashmap 的长度
	private static  int size=0;
	
	
	 public YXHashMap(int length,double  loader){
		 this.defaultLength=length;
		 this.defaultLoader=loader;
		 table = new Entry[defaultLength];
	 }
	 
	 
	 public YXHashMap(){
		 this(defaultLength, defaultLoader);
	 }
	//添加值
	@Override
	public void put(K k, V v) {
		//判断是否需要扩容
		if (size >= defaultLength* defaultLoader) {
			doubleSize();
		}
		
		
		int index = index(k);
		
		if (table[index] == null ) {
			table[index] =newEntry(k,v,null);
		} else {
			table[index] =newEntry(k,v,table[index]);
		}
		size++;
	}
	/**
	 * 
	 * @param k
	 * @param v
	 * @param next 老的数据，加入到链表中
	 * @return
	 */
	private Entry<K,V> newEntry(K k,V v,Entry<K,V> next){
		return new Entry(k,v,next);				
	}
	
	private void doubleSize(){
		//扩容第一步
		Entry<K, V>[] newTable = new Entry[defaultLength* 2];
		//扩容第二步将辣的数组在新的数组中进行再散列排序
		againTable(newTable);
	}
	/**
	 * 
	 * @param newTable 扩容之后的底层数组
	 */
	private void againTable(Entry<K, V>[] newTable){
		List<Entry<K,V>> list = new ArrayList<Entry<K,V>>();
		for (int i = 0; i < table.length; i++) {
			if (table[i] == null ) {
				continue;
			} else {
				list.add(table[i]);
			}
		}
		if(list.size()>0){
			size=0;
			defaultLength =defaultLength *2;
			table =newTable;
			for (Entry<K, V> entry : list) {
				//存在疑问
				if(entry.next != null){
					entry.next = null;
				}
				put(entry.getKey(), entry.getValue());
			}
		}
	}
	
	private int index(K k){
		
		int index = k.hashCode()%defaultLength;
		
		//存在负数这里有疑问-----------------------------------
		return index >=0?index :-index;
	}
	//根据KEY获取值
	@Override
	public V get(K k) {
		int  index = index(k);
		Entry< K, V> next= table[index];
		
 		if (next == null ) {
			return null;
		}
		 return findValueByEntryKey(k,table[index]);
	}

	private V findValueByEntryKey(K k,Entry< K, V> next){
		//该位置上存在链表结构进行递归
		if(k == next.getKey() || k.equals(next.getKey())){
			return next.getValue();
		}else {
			if(next.next != null){
				findValueByEntryKey(k,next.next);
			}
		}
		return null;
	}
	@Override
	public int size() {
		return size;
	}
	class Entry<K, V> implements YXMap.Entry<K, V>{
		K k;
		V v;
		Entry<K,V> next;
		
		private Entry(K k,V v,Entry<K,V> next){
			this.k=k;
			this.v= v;
			this.next = next;
		}
		
		@Override
		public K getKey() {
			return k;
		}

		@Override
		public V getValue() {
			return v;
		}
		
	}
}
