package cn.leyundong.activity;

import java.io.IOException;
import java.io.InputStream;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import net.sourceforge.pinyin4j.PinyinHelper;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cn.leyundong.R;
import cn.leyundong.entity.City;
import cn.leyundong.view.CustomGridView;

/**
 * 城市切换
 * @author Administrator
 *
 */
public class CitysActivity extends Activity{
	
	public final static String SER_KEY = "cn.leyundong.ser";  
	
	private ListView lvCitys;
	
	private List<Pair<String, List<City>>> mCitys;
	private Pair<String, List<City>> mAtoGPairCitys;
	private Pair<String, List<City>> mHtoNPairCitys;
	private Pair<String, List<City>> mOtoTPairCitys;
	private Pair<String, List<City>> mUtoZPairCitys;
	private List<City> mAtoGCitys;
	private List<City> mHtoNCitys;
	private List<City> mOtoTCitys;
	private List<City> mUtoZCitys;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.citys);
		
		initCitysDataInAsset();
		
		initViews();
		
	}
	
	private void initViews(){
		
		lvCitys = (ListView)findViewById(R.id.lvCitys);
		lvCitys.setAdapter(new CitysAdapter(mCitys));
		
	}
	
	private void initCitysDataInAsset() {
		
		mCitys = new ArrayList<Pair<String, List<City>>>();
		mAtoGCitys = new ArrayList<City>();
		mHtoNCitys = new ArrayList<City>();
		mOtoTCitys = new ArrayList<City>();
		mUtoZCitys = new ArrayList<City>();
		
		InputStream in = null;
		try {
			in = getAssets().open("citys.txt");
			Properties p = new Properties();
			p.load(in);
			Set<Object> keys = p.keySet();
			for (Object key : keys) {
				City objCity = new City();
				String cityname = new String(((String)key).getBytes("ISO8859-1"),"utf-8");
				String citycode = (String) p.get(key);
				objCity.setCsmc(cityname);
				objCity.setCsdm(citycode);
				//citys.add(objCity);
				
				String firstword = getPinYinHeadChar(cityname);
				if(firstword.equals("a")||firstword.equals("b")||firstword.equals("c")||firstword.equals("d")||firstword.equals("e")||firstword.equals("f")||firstword.equals("g")){
					mAtoGCitys.add(objCity);
				}else if(firstword.equals("h")||firstword.equals("i")||firstword.equals("j")||firstword.equals("k")||firstword.equals("l")||firstword.equals("m")||firstword.equals("n")){
					mHtoNCitys.add(objCity);
				}else if(firstword.equals("o")||firstword.equals("p")||firstword.equals("q")||firstword.equals("r")||firstword.equals("s")||firstword.equals("t")){
					mOtoTCitys.add(objCity);
				}else if(firstword.equals("u")||firstword.equals("v")||firstword.equals("w")||firstword.equals("x")||firstword.equals("y")||firstword.equals("z")){
					mUtoZCitys.add(objCity);
				}
			}
			
			Comparator<City> com = new ChineseCharComparator();
			Collections.sort(mAtoGCitys, com);
			Collections.sort(mHtoNCitys, com);
			Collections.sort(mOtoTCitys, com);
			Collections.sort(mUtoZCitys, com);
			//System.out.println(mAtoGCitys);
			mAtoGPairCitys = new Pair<String, List<City>>("A-B-C-D-E-F-G", mAtoGCitys);
			mHtoNPairCitys = new Pair<String, List<City>>("H-I-J-K-L-M-N", mHtoNCitys);
			mOtoTPairCitys = new Pair<String, List<City>>("O-P-Q-R-S-T", mOtoTCitys);
			mUtoZPairCitys = new Pair<String, List<City>>("U-V-W-X-Y-Z", mUtoZCitys);
			mCitys.add(mAtoGPairCitys);
			mCitys.add(mHtoNPairCitys);
			mCitys.add(mOtoTPairCitys);
			mCitys.add(mUtoZPairCitys);
			//mCitys.add();
			
			System.out.println("fuck----------------------");
			
			p.clear();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	/**
	  * 获取字符串中中文字符的首字母
	  * @param str
	  * @return
	  */
	private String getPinYinHeadChar(String str) {
		String convert = "";
		for (int j = 0; j < str.length(); j++) {
			char word = str.charAt(j);
			//char word = str.charAt(0);

			String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
			if (pinyinArray != null) {
				convert = convert + pinyinArray[0].charAt(0);
			} else {
				convert = convert + word;
			}
			break;
		}
		return convert;
	}
	
	class ChineseCharComparator implements Comparator<City>{

		Collator cityCollator = Collator.getInstance(java.util.Locale.CHINA);
		
		@Override
		public int compare(City c1, City c2) {
			if (cityCollator.compare(c1.getCsmc(), c2.getCsmc()) < 0)
				return -1;
			else if (cityCollator.compare(c1.getCsmc(), c2.getCsmc()) > 0)
				return 1;
			else
				return 0;
		}
		
	}
	
	
	/**
	 * 城市列表数据源
	 * @author Administrator
	 *
	 */
	class CitysAdapter extends BaseAdapter{
		private List<Pair<String, List<City>>> citys;
		
		public CitysAdapter(List<Pair<String, List<City>>> paramCitys){
			citys = new ArrayList<Pair<String, List<City>>>();
			citys = paramCitys;
		}

		@Override
		public int getCount() {
			return citys.size();
		}

		@Override
		public Object getItem(int position) {
			return citys.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if(convertView == null){
				
				convertView = LayoutInflater.from(CitysActivity.this).inflate(R.layout.citys_item, null);
				holder = new ViewHolder();
				holder.title = (TextView)convertView.findViewById(R.id.city_title);
				holder.groupcitys = (CustomGridView)convertView.findViewById(R.id.gvCitys);
				convertView.setTag(holder);
				
			}else{
				holder = (ViewHolder)convertView.getTag();
			}
			
			holder.title.setText(citys.get(position).first);
			final GridViewCitysAdapter gvCityAdapter = new GridViewCitysAdapter(CitysActivity.this, citys.get(position).second);
			holder.groupcitys.setAdapter(gvCityAdapter);
			holder.groupcitys.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					City objCity = (City)gvCityAdapter.getItem(position);
					Toast.makeText(CitysActivity.this, "click"+objCity.getCsmc() + "=" + objCity.getCsdm(), 1).show();
					
			        Intent intent = new Intent();  
			        //String citycode = objCity.getCsdm();
			        //String cityname = objCity.getCsmc();
//			        Bundle bundle = new Bundle();  
			        //bundle.putString("citycode", citycode);
			        //bundle.putString("cityname", cityname);
//			        intent.putExtras(bundle);  
//			        bundle.putSerializable(SER_KEY,objCity);   
			        intent.putExtra(City.class.getName(), objCity);
			          
			        CitysActivity.this.setResult(RESULT_OK, intent);
			        CitysActivity.this.finish();
				}
				
			});
			
			return convertView;
		}
		
	}
	
	static class ViewHolder{
		TextView title;
		CustomGridView groupcitys;
	}
	
	
	/**
	 * 分组gridview数据源
	 * @author andy.chen
	 *
	 */
	class GridViewCitysAdapter extends BaseAdapter{
		private Context mContext;
		private List<City> groupCitys;
		
		public GridViewCitysAdapter(Context ctx, List<City> paramCitys){
			mContext = ctx;
			groupCitys = new ArrayList<City>();
			groupCitys = paramCitys;
		}

		@Override
		public int getCount() {
			return groupCitys.size();
		}

		@Override
		public Object getItem(int position) {
			return groupCitys.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView tvCity;
	        if (convertView == null) {  // if it's not recycled, initialize some attributes
	        	tvCity = new TextView(mContext);
	        } else {
	        	tvCity = (TextView) convertView;
	        }
	        tvCity.setText(groupCitys.get(position).getCsmc());
	        tvCity.setGravity(Gravity.CENTER);
			return tvCity;
		}
		
	}


}
