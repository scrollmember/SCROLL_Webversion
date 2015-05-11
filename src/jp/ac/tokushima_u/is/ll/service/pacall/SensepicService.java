package jp.ac.tokushima_u.is.ll.service.pacall;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.ac.tokushima_u.is.ll.common.page.Page;
import jp.ac.tokushima_u.is.ll.common.page.PageRequest;
import jp.ac.tokushima_u.is.ll.common.page.PageUtils;
import jp.ac.tokushima_u.is.ll.entity.pacall.PicPtype;
import jp.ac.tokushima_u.is.ll.entity.pacall.Ptype;
import jp.ac.tokushima_u.is.ll.entity.pacall.SenseData;
import jp.ac.tokushima_u.is.ll.entity.pacall.SensePic;
import jp.ac.tokushima_u.is.ll.mapper.pacall.PicPtypeMapper;
import jp.ac.tokushima_u.is.ll.mapper.pacall.PtypeMapper;
import jp.ac.tokushima_u.is.ll.mapper.pacall.SensepicMapper;
import jp.ac.tokushima_u.is.ll.util.PacallConstants;
import jp.ac.tokushima_u.is.ll.util.Utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SensepicService {
	
	@Autowired
	private PtypeMapper ptypeMapper;
	
	@Autowired
	private SensepicMapper sensepicMapper;
	
	@Autowired
	private PicPtypeMapper picPtypeMapper;

	public List<SensePic> addSensepicToDatabase(String folderId, List<SenseData> sensedataList) {
		List<SensePic> picList = new ArrayList<SensePic>();
		
		int i = 0;
		for(SenseData senseData: sensedataList){
			if (PacallConstants.CAM.equals(senseData.getKey())) {
				SensePic pic = new SensePic();
				pic.setId(Utility.getUuidKey());
				pic.setFolderId(folderId);
				pic.setIndex(i);
				pic.setDate(senseData.getDate());
				pic.setCam(senseData.getV1().toLowerCase());
				String reason = senseData.getV2();
				if ("P".equals(reason) || "PIR".equals(reason)) {
					pic.setReason(SensePic.Reason.PIR);
				} else if ("T".equals(reason) || "TIM".equals(reason)) {
					pic.setReason(SensePic.Reason.TIM);
				} else if ("M".equals(reason) || "MAN".equals(reason)) {
					pic.setReason(SensePic.Reason.MAN);
				} else if ("L".equals(reason) || "CLR".equals(reason)) {
					pic.setReason(SensePic.Reason.CLR);
				} else if ("MAG".equals(reason)){
					pic.setReason(SensePic.Reason.MAG);
				} else if ("ACC".equals(reason)){
					pic.setReason(SensePic.Reason.ACC);
				}
				picList.add(pic);
			}
			i++;
		}
		
		for (SensePic pic : picList) {
			findAndSetAcc(pic, sensedataList);
			findAndSetTmp(pic, sensedataList);
			findAndSetClr(pic, sensedataList);
			findAndSetMag(pic, sensedataList);
			sensepicMapper.insert(pic);
		}
		return picList;
	}

	public void classifySensePic(List<SensePic> data) {
		Ptype manual = ptypeMapper.selectByName(PacallConstants.MANUAL);
		Ptype dark = ptypeMapper.selectByName(PacallConstants.DARK);
		Ptype shake = ptypeMapper.selectByName(PacallConstants.SHAKE);
		Ptype duplicate = ptypeMapper.selectByName(PacallConstants.DUPLICATE);
		Ptype normal = ptypeMapper.selectByName(PacallConstants.NORMAL);
		
		for(int i=0;i<data.size();i++){
			SensePic pic = data.get(i);
			if(SensePic.Reason.MAN==pic.getReason()){
				picPtypeMapper.insert(new PicPtype(pic.getId(), manual.getId()));
			//}else if((pic.getName().length()<15 && pic.getClr()<20d) || (pic.getName().length()>=15 && pic.getClr()<20d)){
			}else if(pic.getClr()<20){
				picPtypeMapper.insert(new PicPtype(pic.getId(), dark.getId()));
			}else if(pic.getAcc()>1 && pic.getClr()<50d){
				picPtypeMapper.insert(new PicPtype(pic.getId(), shake.getId()));
			}else if(isDuplicatedWithPrevious(pic, data, i)){
				picPtypeMapper.insert(new PicPtype(pic.getId(), duplicate.getId()));
			}else{
				picPtypeMapper.insert(new PicPtype(pic.getId(), normal.getId()));
			}
		}
	}

	private void findAndSetMag(SensePic pic, List<SenseData> dataList) {
		Double v1x = null;
		Double v1y = null;
		Double v1z = null;
		Double v2x = null;
		Double v2y = null;
		Double v2z = null;
		Date date1 = pic.getDate();
		Date date2 = pic.getDate();

		for (int i = pic.getIndex() - 1; i >= 0; i--) {
			SenseData data = dataList.get(i);
			if (PacallConstants.MAG.equals(data.getKey())) {
				v1x = Double.valueOf(data.getV1());
				v1y = Double.valueOf(data.getV2());
				v1z = Double.valueOf(data.getV3());
				date1 = data.getDate();
				break;
			}
		}

		for (int i = pic.getIndex() + 1; i < dataList.size(); i++) {
			SenseData data = dataList.get(i);
			if (PacallConstants.MAG.equals(data.getKey())) {
				v2x = Double.valueOf(data.getV1());
				v2y = Double.valueOf(data.getV2());
				v2z = Double.valueOf(data.getV3());
				date2 = data.getDate();
				break;
			}
		}
		
		if(v1x==null && v2x==null){
			v1x=v2x=0d;
		}
		if(v1x==null){
			v1x=v2x;
		}
		if(v2x==null){
			v2x=v1x;
		}
		if(v1y==null && v2y==null){
			v1y=v2y=0d;
		}
		if(v1y==null){
			v1y=v2y;
		}
		if(v2y==null){
			v2y=v1y;
		}
		if(v1z==null && v2z==null){
			v1z=v2z=0d;
		}
		if(v1z==null){
			v1z=v2z;
		}
		if(v2z==null){
			v2z=v1z;
		}

		if (v1x < v2x) {
			pic.setMagX(calAvg(v1x, v2x, date1, date2, pic.getDate()));
		} else {
			pic.setMagX(calAvg(v2x, v1x, date2, date1, pic.getDate()));
		}
		if (v1y < v2y) {
			pic.setMagY(calAvg(v1y, v2y, date1, date2, pic.getDate()));
		} else {
			pic.setMagY(calAvg(v2y, v1y, date2, date1, pic.getDate()));
		}
		if (v1z < v2z) {
			pic.setMagZ(calAvg(v1z, v2z, date1, date2, pic.getDate()));
		} else {
			pic.setMagZ(calAvg(v2z, v1z, date2, date1, pic.getDate()));
		}
	}

	private void findAndSetClr(SensePic pic, List<SenseData> dataList) {
		Date date1 = pic.getDate();
		Date date2 = pic.getDate();
		Double v1 = null;
		Double v2 = null;

		for (int i = pic.getIndex() - 1; i >= 0; i--) {
			SenseData data = dataList.get(i);
			if (PacallConstants.CLR.equals(data.getKey())) {
				v1 = Double.valueOf(data.getV1());
				date1 = data.getDate();
				break;
			}
		}

		for (int i = pic.getIndex() + 1; i < dataList.size(); i++) {
			SenseData data = dataList.get(i);
			if (PacallConstants.CLR.equals(data.getKey())) {
				v2 = Double.valueOf(data.getV1());
				date2 = data.getDate();
				break;
			}
		}

		if(v1==null && v2==null){
			v1=v2=0d;
		}
		if(v1==null){
			v1=v2;
		}
		if(v2==null){
			v2=v1;
		}
		
		if (v1 < v2) {
			pic.setClr(calAvg(v1, v2, date1, date2, pic.getDate()));
		} else {
			pic.setClr(calAvg(v2, v1, date2, date1, pic.getDate()));
		}
	}

	private void findAndSetTmp(SensePic pic, List<SenseData> dataList) {
		Date date1 = pic.getDate();
		Date date2 = pic.getDate();
		Double v1 = null;
		Double v2 = null;

		for (int i = pic.getIndex() - 1; i >= 0; i--) {
			SenseData data = dataList.get(i);
			if (PacallConstants.TMP.equals(data.getKey())) {
				v1 = Double.valueOf(data.getV1());
				date1 = data.getDate();
				break;
			}
		}

		for (int i = pic.getIndex() + 1; i < dataList.size(); i++) {
			SenseData data = dataList.get(i);
			if (PacallConstants.TMP.equals(data.getKey())) {
				v2 = Double.valueOf(data.getV1());
				date2 = data.getDate();
				break;
			}
		}
		
		if(v1==null && v2==null){
			v1=v2=0d;
		}
		if(v1==null){
			v1=v2;
		}
		if(v2==null){
			v2=v1;
		}

		if (v1 < v2) {
			pic.setTmp(calAvg(v1, v2, date1, date2, pic.getDate()));
		} else {
			pic.setTmp(calAvg(v2, v1, date2, date1, pic.getDate()));
		}

	}

	private void findAndSetAcc(SensePic pic, List<SenseData> dataList) {
		Double v1x = null;
		Double v1y = null;
		Double v1z = null;
		Double v2x = null;
		Double v2y = null;
		Double v2z = null;
		Date date1 = pic.getDate();
		Date date2 = pic.getDate();

		for (int i = pic.getIndex() - 1; i >= 0; i--) {
			SenseData data = dataList.get(i);
			if (PacallConstants.ACC.equals(data.getKey())) {
				v1x = Double.valueOf(data.getV1());
				v1y = Double.valueOf(data.getV2());
				v1z = Double.valueOf(data.getV3());
				date1 = data.getDate();
				break;
			}
		}

		for (int i = pic.getIndex() + 1; i < dataList.size(); i++) {
			SenseData data = dataList.get(i);
			if (PacallConstants.ACC.equals(data.getKey())) {
				v2x = Double.valueOf(data.getV1());
				v2y = Double.valueOf(data.getV2());
				v2z = Double.valueOf(data.getV3());
				date2 = data.getDate();
				break;
			}
		}
		
		if(v1x==null && v2x==null){
			v1x=v2x=0d;
		}
		if(v1x==null){
			v1x=v2x;
		}
		if(v2x==null){
			v2x=v1x;
		}
		if(v1y==null && v2y==null){
			v1y=v2y=0d;
		}
		if(v1y==null){
			v1y=v2y;
		}
		if(v2y==null){
			v2y=v1y;
		}
		if(v1z==null && v2z==null){
			v1z=v2z=0d;
		}
		if(v1z==null){
			v1z=v2z;
		}
		if(v2z==null){
			v2z=v1z;
		}

		if (v1x < v2x) {
			pic.setAccX(calAvg(v1x, v2x, date1, date2, pic.getDate()));
		} else {
			pic.setAccX(calAvg(v2x, v1x, date2, date1, pic.getDate()));
		}
		if (v1y < v2y) {
			pic.setAccY(calAvg(v1y, v2y, date1, date2, pic.getDate()));
		} else {
			pic.setAccY(calAvg(v2y, v1y, date2, date1, pic.getDate()));
		}
		if (v1z < v2z) {
			pic.setAccZ(calAvg(v1z, v2z, date1, date2, pic.getDate()));
		} else {
			pic.setAccZ(calAvg(v2z, v1z, date2, date1, pic.getDate()));
		}
	}
	
	private Double calAvg(Double v1, Double v2, Date date1, Date date2,
			Date date) {
		double t1 = date.getTime()-date1.getTime();
		double t2 = date2.getTime()-date.getTime();
		double v = t1*(v2-v1)/(t1+t2)+v1;
		return v;
	}
	
	private boolean isDuplicatedWithPrevious(SensePic pic, List<SensePic> data, int index) {
		for(int i=0;i<index;i++){
			SensePic pre = data.get(i);
			if(3600000 < pre.getDate().getTime()-pic.getDate().getTime()){
				continue;
			}
//			System.out.println("[CLR]\t"+pic.getName()+"\t"+pre.getName()+"\t"+Math.abs(pre.getClr()-pic.getClr()));
//			System.out.println("[TMP]\t"+pic.getName()+"\t"+pre.getName()+"\t"+Math.abs(pre.getTmp()-pic.getTmp()));
//			System.out.println("[ACC]\t"+pic.getName()+"\t"+pre.getName()+"\t"+isNearAcc(pre,pic));

			if(Math.abs(pre.getClr()-pic.getClr())<100 && Math.abs(pre.getTmp()-pic.getTmp())<3 && isNearAcc(pre,pic) && isNearMag(pre, pic) ){
				pic.setSamePicId(pre.getId());
				return true;
			}
		}
		return false;
	}
	
	private boolean isNearAcc(SensePic pre, SensePic pic) {
		double accDiff =  cal3AxisDiff(pre.getAccX(), pre.getAccY(), pre.getAccZ(), pic.getAccX(),pic.getAccY(), pic.getAccZ());
//		System.out.println("[ACC]\t"+pic.getName()+"\t"+pre.getName()+"\t"+accDiff);
		if(5E-5 > accDiff){
			return true;
		}else{
			return false;
		}
	}
	
	private double cal3AxisDiff(Double x1, Double y1, Double z1,
			Double x2, Double y2, Double z2) {
		return Math.pow((x1-x2),2)+Math.pow((y1-y2),2)+Math.pow((z1-z2),2);
	}

	private boolean isNearMag(SensePic pre, SensePic pic) {
		double magDiff = cal3AxisDiff(pre.getMagX(), pre.getMagY(), pre.getMagZ(), pic.getMagX(),pic.getMagY(), pic.getMagZ());
		//System.out.println("[ACC]\t"+pic.getName()+"\t"+pre.getName()+"\t"+magDiff);

		if(600 > magDiff ){
			return true;
		}else{
			return false;
		}
	}

	public Page<SensePic> findPage(String folderId, String type, Integer page, Integer pageSize) {
		PageRequest<SensePic> request = new PageRequest<SensePic>(page, pageSize, "date asc");
		request.addAttribute("ptype", type);
		request.addAttribute("folderId", folderId);
		List<SensePic>  resultList = sensepicMapper.selectByRequest(request);
		long resultCount =  sensepicMapper.countTotalByRequest(request);
		return PageUtils.buildPage(request, resultList, resultCount);
	}

	public Map<String, Object> countEachType(String folderId) {
		List<Map<String, Object>> data = sensepicMapper.countGroupByType(folderId);
		Map<String, Object> result = new HashMap<String, Object>();
		for(Map<String, Object> d: data){
			result.put(String.valueOf(d.get("name")), d.get("number"));
		}
		if(!result.containsKey(PacallConstants.MANUAL)){
			result.put(PacallConstants.MANUAL, 0);
		}
		if(!result.containsKey(PacallConstants.NORMAL)){
			result.put(PacallConstants.NORMAL, 0);
		}
		if(!result.containsKey(PacallConstants.DUPLICATE)){
			result.put(PacallConstants.DUPLICATE, 0);
		}
		if(!result.containsKey(PacallConstants.SHAKE)){
			result.put(PacallConstants.SHAKE, 0);
		}
		if(!result.containsKey(PacallConstants.DARK)){
			result.put(PacallConstants.DARK, 0);
		}
		result.put("all", countByFolder(folderId));
		return result;
	}

	@Transactional(readOnly = true)
	public long countByFolder(String folderId) {
		return sensepicMapper.selectCountByFolder(folderId);
	}

	public SensePic findById(String sensepicid) {
		return sensepicMapper.selectById(sensepicid);
	}

	public SensePic findByName(String folderId, String filename) {
		return sensepicMapper.selectByName(folderId, filename);
	}

	public void updateFileId(String sensePicId, String fileId) {
		sensepicMapper.updateFileId(sensePicId, fileId);
	}
}
