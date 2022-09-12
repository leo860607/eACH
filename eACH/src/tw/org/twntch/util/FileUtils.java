package tw.org.twntch.util;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.Calendar;
/**
 * 
 * @author Hugo
 * 只適用jdk1.7+
 */
public class FileUtils {

	
	/**
	 * 
	 * @param filepath 檔案來源
	 * @param timeformat 預設為yyyy-MM-dd HH:mm:ss
	 * @param type 0:檔案建立日期 ，1:檔案最後修改日期，2:檔案最後存取日期
	 * @return
	 */
	public static  String getCTime(String filepath , String timeformat , int type){
		String time = "";
		String tmp = "";
		String format = StrUtils.isEmpty(timeformat)? "yyyy-MM-dd HH:mm:ss" :timeformat;
		Path path = null;
		BasicFileAttributes attrs = null;
		Calendar calendar = Calendar.getInstance();
        try {
        	path = Paths.get(filepath);
			attrs = Files.readAttributes(path, BasicFileAttributes.class);
			switch (type) {
			case 0:
				tmp = " 建立時間為：";
				calendar.setTimeInMillis(attrs.creationTime().toMillis());
				break;
			case 1:
				tmp = " 最後修改時間為：";
				calendar.setTimeInMillis(attrs.lastModifiedTime().toMillis());
				break;
			case 2:
				tmp = " 最後存取時間為：";
				calendar.setTimeInMillis(attrs.lastAccessTime().toMillis());
				break;

			default:
				tmp = " 建立時間為：";
				calendar.setTimeInMillis(attrs.creationTime().toMillis());
				break;
			}
			time = new SimpleDateFormat(format).format(calendar.getTime());
			//System.out.println(filepath + tmp+ time);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return time;
	}
	
	public static void main(String[] args) throws IOException{
    
		FileUtils.getCTime("G:/eACH/db2_back.txt", "", 0);
		
		
	}
	
	
}
