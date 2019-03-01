package hashMapDemo;

import java.util.ArrayList;
import java.util.List;
/**
 * @author yangXing
 * @param <K>
 * @param <V>
 */
public class HashMapDemo<K, V> implements MapDemo<K, V> {

	//底层是数组，默认的长度的是16、//   key%m的值不会大于16  也就是
	//	HashMap的默认长度为16,是为了降低hash碰撞的几率
	private  static int defaultLength =16;
	
	//默认的负载因子是0.75   存储的长度达到    16 ……  0.75的时候就需要自动扩容
	private  static double defaultLoader=0.75;
	
	//对应着链表结构   
	private Entry<K,V>[] table =null;
	
	//初始的创建默认长度是0、
	private int   size = 0;
	/**
	 * 
	 * @param length  长度
	 * @param loader  负载因子
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
		//判断下size是否达到了需要扩容的值
		if(size >= defaultLength*defaultLoader){
			up2size();
		}
		// 第一步     
		int  index =getIndex(k);
		Entry<K , V> entry = table[index];
		//判断已有的数组中这个index位置是否含数值
		if (entry == null) {
			//没有数值，直接添加在数值上  已存有数据的长度加一
			table[index] = newEntry(k,v,null);
		} else {
			//数组上这个位置不为null   
			//说明index位置有元素，那么就要进行一个替换。然后next指向老数据
			//当前位置存在数据，及在数组的当前位置的数据进行链表处理，将新的数据放置在数组的对应位置上，并且新数据的next指向老数据
			//疑问：如果两个key通过hash%Entry[].length得到的index相同，会不会有覆盖的危险？
			//这里HashMap里面用到链式数据结构的一个概念。上面我们提到过Entry类里面有一个next属性，
			//  作用是指向下一个Entry。打个比方， 第一个键值对A进来，通过计算其key的hash得到的index=0，
			//  记做:Entry[0] = A。一会后又进来一个键值对B，通过计算其index也等于0，
			//  现在怎么办？HashMap会这样做:B.next = A,Entry[0] = B,如果又进来C,
			//index也等于0,那么C.next = B,Entry[0] = C；
			//这样我们发现index=0的地方其实存取了A,B,C三个键值对,他们通过next这个属性链接在一起。
			//所以疑问不用担心。也就是说数组中存储的是最后插入的元素。
			table[index] = newEntry(k,v,entry);
		}
		size++;
		return table[index].getValue();
	}

	private  Entry<K,V> newEntry(K k,V v,Entry<K ,V> next){
		return new Entry(k,v,next);
		
	}
	
	private void  up2size(){
		//扩容就是扩table
		//新创建数组之后，以前老数组的数据要在新数组中再散列
		Entry<K ,V>[] newTable = new Entry[2*defaultLength] ;
		agingHash(newTable);
	}
	
	private void agingHash(Entry<K, V>[] newTable){
		List<Entry<K,V>> list = new ArrayList<Entry<K,V>>();
		
		for(int  i=0;i<table.length;i++){
			if (table[i] == null) {
				continue;
			} else {
				//数组中的链表结构一起复制到集合中
				findEntryNext(table[i],list);
			}
		}
		
		//应该判断的值是list.size=size  判断是否全部赋值成功 疑问已解决   
		if(list.size()> 0){
			//对老的数组进行归零，新的数组进行再散列
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
		//创建一个hash函数，根据key和hash函数算出数组的下标
		
		int  index = getIndex(k);
		//未根据key值找到对应的位置
		if(table[index] == null){
			return null;
		}
		return findValueByEntryKey(k,table[index]);
	}
	
	private  V findValueByEntryKey(K k,Entry<K,V> entry){
		//2% 13=2
		//15% 13 =2
		//可能是链表方式存储的，一个数组上三个位置有值，单数存储了20个值，存在链表方式，递归去找
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
	
	
	//用于链表
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
