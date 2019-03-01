package demo;

import java.util.ArrayList;
import java.util.List;

public class YXHashMap<K, V> implements YXMap<K, V> {
	
	private static   int  defaultLength=16;
	
	private static  double defaultLoader=0.75;
	
	//�ײ�Ļ���������
	
	private  Entry<K,V>[] table=null;
	
	//hashmap �ĳ���
	private static  int size=0;
	
	
	 public YXHashMap(int length,double  loader){
		 this.defaultLength=length;
		 this.defaultLoader=loader;
		 table = new Entry[defaultLength];
	 }
	 
	 
	 public YXHashMap(){
		 this(defaultLength, defaultLoader);
	 }
	//���ֵ
	@Override
	public void put(K k, V v) {
		//�ж��Ƿ���Ҫ����
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
	 * @param next �ϵ����ݣ����뵽������
	 * @return
	 */
	private Entry<K,V> newEntry(K k,V v,Entry<K,V> next){
		return new Entry(k,v,next);				
	}
	
	private void doubleSize(){
		//���ݵ�һ��
		Entry<K, V>[] newTable = new Entry[defaultLength* 2];
		//���ݵڶ����������������µ������н�����ɢ������
		againTable(newTable);
	}
	/**
	 * 
	 * @param newTable ����֮��ĵײ�����
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
				//��������
				if(entry.next != null){
					entry.next = null;
				}
				put(entry.getKey(), entry.getValue());
			}
		}
	}
	
	private int index(K k){
		
		int index = k.hashCode()%defaultLength;
		
		//���ڸ�������������-----------------------------------
		return index >=0?index :-index;
	}
	//����KEY��ȡֵ
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
		//��λ���ϴ�������ṹ���еݹ�
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
