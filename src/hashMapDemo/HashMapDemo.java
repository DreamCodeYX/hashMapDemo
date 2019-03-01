package hashMapDemo;

import java.util.ArrayList;
import java.util.List;
/**
 * @author yangXing
 * @param <K>
 * @param <V>
 */
public class HashMapDemo<K, V> implements MapDemo<K, V> {

	//�ײ������飬Ĭ�ϵĳ��ȵ���16��//   key%m��ֵ�������16  Ҳ����
	//	HashMap��Ĭ�ϳ���Ϊ16,��Ϊ�˽���hash��ײ�ļ���
	private  static int defaultLength =16;
	
	//Ĭ�ϵĸ���������0.75   �洢�ĳ��ȴﵽ    16 ����  0.75��ʱ�����Ҫ�Զ�����
	private  static double defaultLoader=0.75;
	
	//��Ӧ������ṹ   
	private Entry<K,V>[] table =null;
	
	//��ʼ�Ĵ���Ĭ�ϳ�����0��
	private int   size = 0;
	/**
	 * 
	 * @param length  ����
	 * @param loader  ��������
	 */
	public  HashMapDemo(int length,double  loader){
		this.defaultLength=length;
		this.defaultLoader=loader;
		table =new Entry[defaultLength];
	}
	public HashMapDemo() {
		this(defaultLength, defaultLoader);
	}
	
	private int getIndex(K k){
		int m = defaultLength;
		int index = k.hashCode() % m;
		return  index >=0? index : -index;
	}
	
	@Override
	public V put(K k, V v) {
		//�ж���size�Ƿ�ﵽ����Ҫ���ݵ�ֵ
		if(size >= defaultLength*defaultLoader){
			up2size();
		}
		// ��һ��     
		int  index =getIndex(k);
		Entry<K , V> entry = table[index];
		//�ж����е����������indexλ���Ƿ���ֵ
		if (entry == null) {
			//û����ֵ��ֱ���������ֵ��  �Ѵ������ݵĳ��ȼ�һ
			table[index] = newEntry(k,v,null);
		} else {
			//���������λ�ò�Ϊnull   
			//˵��indexλ����Ԫ�أ���ô��Ҫ����һ���滻��Ȼ��nextָ��������
			//��ǰλ�ô������ݣ���������ĵ�ǰλ�õ����ݽ������������µ����ݷ���������Ķ�Ӧλ���ϣ����������ݵ�nextָ��������
			//���ʣ��������keyͨ��hash%Entry[].length�õ���index��ͬ���᲻���и��ǵ�Σ�գ�
			//����HashMap�����õ���ʽ���ݽṹ��һ��������������ᵽ��Entry��������һ��next���ԣ�
			//  ������ָ����һ��Entry������ȷ��� ��һ����ֵ��A������ͨ��������key��hash�õ���index=0��
			//  ����:Entry[0] = A��һ����ֽ���һ����ֵ��B��ͨ��������indexҲ����0��
			//  ������ô�죿HashMap��������:B.next = A,Entry[0] = B,����ֽ���C,
			//indexҲ����0,��ôC.next = B,Entry[0] = C��
			//�������Ƿ���index=0�ĵط���ʵ��ȡ��A,B,C������ֵ��,����ͨ��next�������������һ��
			//�������ʲ��õ��ġ�Ҳ����˵�����д洢�����������Ԫ�ء�
			table[index] = newEntry(k,v,entry);
		}
		size++;
		return table[index].getValue();
	}

	private  Entry<K,V> newEntry(K k,V v,Entry<K ,V> next){
		return new Entry(k,v,next);
		
	}
	
	private void  up2size(){
		//���ݾ�����table
		//�´�������֮����ǰ�����������Ҫ������������ɢ��
		Entry<K ,V>[] newTable = new Entry[2*defaultLength] ;
		agingHash(newTable);
	}
	
	private void agingHash(Entry<K, V>[] newTable){
		List<Entry<K,V>> list = new ArrayList<Entry<K,V>>();
		
		for(int  i=0;i<table.length;i++){
			if (table[i] == null) {
				continue;
			} else {
				//�����е�����ṹһ���Ƶ�������
				findEntryNext(table[i],list);
			}
		}
		
		//Ӧ���жϵ�ֵ��list.size=size  �ж��Ƿ�ȫ����ֵ�ɹ� �����ѽ��   
		if(list.size()> 0){
			//���ϵ�������й��㣬�µ����������ɢ��
			size=0;
			
			defaultLength= defaultLength* 2;
			table = newTable;
			for (Entry<K, V> entry : list) {
				if(entry.next != null){
					entry.next = null;
				}
				put(entry.getKey(), entry.getValue());
			}
		}
	}
	
	private  void  findEntryNext(Entry<K,V> entry ,List<Entry<K,V>> list ){
		if(entry !=null && entry.next != null){
			list.add(entry);
			findEntryNext(entry.next, list);
		}else {
			list.add(entry);
		}
	}
	
	@Override
	public V get(K k) {
		//����һ��hash����������key��hash�������������±�
		
		int  index = getIndex(k);
		//δ����keyֵ�ҵ���Ӧ��λ��
		if(table[index] == null){
			return null;
		}
		return findValueByEntryKey(k,table[index]);
	}
	
	private  V findValueByEntryKey(K k,Entry<K,V> entry){
		//2% 13=2
		//15% 13 =2
		//����������ʽ�洢�ģ�һ������������λ����ֵ�������洢��20��ֵ����������ʽ���ݹ�ȥ��
		if(k == entry.getKey() || k.equals(entry.getKey())) {
			return entry.getValue();
		}
		else {
			if (entry.next  !=null) {
				return findValueByEntryKey(k, entry.next);
			}
		}
		return null;
	}
	
	@Override
	public int size() {
		return size;
	}
	
	
	//��������
	class Entry<K, V> implements MapDemo.Entry<K, V> {
		K k;
		V v;
		Entry<K, V> next;

		public Entry(K k, V v, Entry<K, V> next) {
			super();
			this.k = k;
			this.v = v;
			this.next = next;
		}

		@Override
		public K getKey() {
//			return null;
			return k;
		}

		@Override
		public V getValue() {
//			return null;
			return v;
		}

	}

}
