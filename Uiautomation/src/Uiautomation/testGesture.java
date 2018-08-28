package Uiautomation;

import java.util.List;
import java.util.NoSuchElementException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;

public class testGesture {
	static int width;
    static int height;
    static String actualparameter;
    static String actualelement;
    static String returnMessage = "";
    
    //向左右上下滑动屏幕
    public void swipeTo(String actualparameter,AndroidDriver driver) throws InterruptedException {
    	width = driver.manage().window().getSize().width;
        height = driver.manage().window().getSize().height;
    	switch (actualparameter)
    	{
    	case "向左滑动":
    		Thread.sleep(1000);
    		driver.swipe(width*11/12, height / 2, width/20, height / 2, 2500);
    		Thread.sleep(1000);
    		break;
    	case "向右滑动":
    		Thread.sleep(1000);
    		driver.swipe(width/10, height / 2, width*9/10, height / 2, 2000);
    		Thread.sleep(1000);
    		break;
    	case "向上滑动":
    		Thread.sleep(1000);
    		driver.swipe(width / 2, height*8 / 10, width / 2, height /10, 1000);
    		Thread.sleep(1000);
    		break;
    	case "向下滑动":
    		Thread.sleep(1000);
    		driver.swipe(width / 2, height*3 / 10, width / 2, height*9 /10, 1000);
    		Thread.sleep(1000);
    		break;
    	default :
    		break;
    	}	
    }

    //手势密码
    public static void patternLock(String locklock,List<WebElement> bot,AndroidDriver driver){
    	final TouchAction touchAction = new TouchAction(driver);

    	String[] alock = new String[locklock.length()];//alock数组用于存放手势字符串分开后单个赋值（手势数字，类型为string）
    	int[] block = new int[locklock.length()];//block数组用于将字符串的手势数字转换为int型
    	for (int i = 0; i < locklock.length(); i++) {
    		alock[i] = String.valueOf(locklock.charAt(i));//char转换为string
    		block[i] = Integer.parseInt(alock[i]);//String转换成int类型;
		}
    	
    	int i = 0;
		switch (locklock.length())//手势密码的长度（9个点从0开始到8）
    	{
    	case 4:
    		touchAction.press(bot.get(block[i])).waitAction(1500).moveTo(bot.get(block[i+1])).moveTo(bot.get(block[i+2]))
    		.moveTo(bot.get(block[i+3])).release();
	        touchAction.perform();
    		break;
    	case 5:
    		touchAction.press(bot.get(block[i])).waitAction(1500).moveTo(bot.get(block[i+1])).moveTo(bot.get(block[i+2]))
    		.moveTo(bot.get(block[i+3])).moveTo(bot.get(block[i+4])).release();
	        touchAction.perform();
    		break;
    	case 6:
    		touchAction.press(bot.get(block[i])).waitAction(1500).moveTo(bot.get(block[i+1])).moveTo(bot.get(block[i+2]))
    		.moveTo(bot.get(block[i+3])).moveTo(bot.get(block[i+4])).moveTo(bot.get(block[i+5])).release();
	        touchAction.perform();
    		break;
    	case 7:
    		touchAction.press(bot.get(block[i])).waitAction(1500).moveTo(bot.get(block[i+1])).moveTo(bot.get(block[i+2]))
    		.moveTo(bot.get(block[i+3])).moveTo(bot.get(block[i+4])).moveTo(bot.get(block[i+5])).moveTo(bot.get(block[i+6])).release();
	        touchAction.perform();
    		break;
    	case 8:
    		touchAction.press(bot.get(block[i])).waitAction(1500).moveTo(bot.get(block[i+1])).moveTo(bot.get(block[i+2]))
    		.moveTo(bot.get(block[i+3])).moveTo(bot.get(block[i+4])).moveTo(bot.get(block[i+5])).moveTo(bot.get(block[i+6])).moveTo(bot.get(block[i+7])).release();
	        touchAction.perform();
    		break;
    	case 9:
    		touchAction.press(bot.get(block[i])).waitAction(1500).moveTo(bot.get(block[i+1])).moveTo(bot.get(block[i+2]))
    		.moveTo(bot.get(block[i+3])).moveTo(bot.get(block[i+4])).moveTo(bot.get(block[i+5])).moveTo(bot.get(block[i+6])).moveTo(bot.get(block[i+7])).moveTo(bot.get(block[i+8])).release();
	        touchAction.perform();
    		break;
    	default :
    		break;
    	}		
    }
    
    //向下滑动查找到某元素为止，可以翻页查找
    public static void swipeToelement(String actualelement, AndroidDriver driver,String actualparameter,List<WebElement> bot) throws InterruptedException {
    	width = driver.manage().window().getSize().width;
        height = driver.manage().window().getSize().height;
    	boolean a = false;
    	int counter = 0;
    	String totalText=driver.getPageSource();  //滑动前获取pagesource			
		while(!a && counter <30){ //如果找不到，最多找10遍
			if(totalText.contains(actualparameter))
			{
			   bot = driver.findElements(By.id(actualelement));
			   for(int i = 0; i<bot.size(); i++ ){
				   String proTitle = bot.get(i).getText();
				   System.out.print(proTitle+i+'\n');
				   if(proTitle.contains(actualparameter)){
					   System.out.print("找到"+proTitle+'\n');   
					   a = true;
					   driver.swipe(width / 2, height*8/20, width / 2, height*6/20, 5000); //找到元素以后继续往下滑动一下，不然可能找不到标题
					   break;
				   }		  
			   }
			}					   
		   if(a == false){
			    System.out.print("没有找到"+actualparameter+"!"+'\n');
		        driver.swipe(width / 2, height*9/10, width / 2, height*4/10, 5000);
		        Thread.sleep(1000);
		   }
		   counter = counter +1;
		   totalText=driver.getPageSource();  //重新获取pagesource
		}
     }
    //滑动查找到某元素为止，可以翻页查找,可以通过mode参数配置向上下左右四个不同方向进行滑动
    public static void swipeToelementAllD(String actualelement, AndroidDriver driver,String actualparameter,List<WebElement> bot,String mode) throws InterruptedException {
    	width = driver.manage().window().getSize().width;
        height = driver.manage().window().getSize().height;
    	boolean a = false;
    	int count=0;
		while(!a && count<30){
			bot = driver.findElements(By.id(actualelement));
		   for(int i = 0; i<bot.size(); i++ ){
			   String proTitle = bot.get(i).getText();
			   System.out.print(proTitle+i+'\n');
			   if(proTitle.contains(actualparameter)){
				   System.out.print("找到"+proTitle+'\n');   
				   a = true;
				   break;
			   }		  
		   }
		   
		   if(a == false){
			    System.out.print("没有找到"+actualparameter+"!"+'\n');
				Thread.sleep(3000);
				switch (mode) {
				case "向下":
					driver.swipe(width / 2, height*8/10, width / 2, height*2/10, 2000); 
					break;
				case "向上":
					driver.swipe(width / 2, height*3/10, width / 2, height*8/10, 2000); 
					break;
				case "向左":
					driver.swipe(width *8/10, height / 2, width *1/10, height / 2, 500); 
					break;
				case "向右":
					driver.swipe(width *1/10, height / 2, width *8/10, height / 2, 500); 
					break;
				default:
					break;
				}
		        
		        Thread.sleep(1000);
		   }
		   count++;
		}
		if(count == 10)
		{
			returnMessage = "没找到要点击的文本信息";
		}
     }

    
    //滑动查找到某元素为止，并点击该元素；可以翻页查找
    public static void swipeAndClick(String actualelement, AndroidDriver driver,String actualparameter,String direction) throws InterruptedException 
    {
    	width = driver.manage().window().getSize().width;
        height = driver.manage().window().getSize().height;
    	boolean findZhangJie = false;
    	int counter = 0;
    	List<WebElement> bot;
    	String totalText=driver.getPageSource();  //滑动前获取pagesource
		while(!findZhangJie && counter <30){ //如果找不到，最多找10遍
			if(totalText.contains(actualparameter))
			{
				bot = driver.findElements(By.id(actualelement));
				   for(int i = 0; i<bot.size(); i++ ){
					   String proTitle = bot.get(i).getText();
					   if(proTitle.contains(actualparameter))
					   {
						   System.out.print("找到"+proTitle+'\n');   
						   bot.get(i).click();
						   findZhangJie = true;
						   //driver.swipe(width / 2, height*8/10, width / 2, height*5/10, 5000); //找到元素以后继续往下滑动一下，不然可能找不到标题
						   break;
					   }		  
				   }
			}
		   if(findZhangJie == false){
			    System.out.print("没有找到"+actualparameter+"!"+'\n');
				Thread.sleep(3000);
				if(direction.equals("down"))
				{
					driver.swipe(width / 2, height*4/10, width / 2, height*9/10, 5000); //向上滑
				}
				else
				{
					driver.swipe(width / 2, height*9/10, width / 2, height*4/10, 5000); //向下滑
				}	
		        Thread.sleep(1000);
		   }
		   totalText=driver.getPageSource();  //重新获取pagesource
		   counter = counter +1;
		}
		if(counter == 10)
		{
			returnMessage = "没找到要点击的文本信息";
		}
     }
    //轻触屏幕中央，弹出阅读器的设置页面
    public static void getReadSet(AndroidDriver driver, String appType, String oprType)
    {
    	width = driver.manage().window().getSize().width;
        height = driver.manage().window().getSize().height;
    	boolean setDisplayed = false;
    	int counter = 0;
    	String displayedText="",displayedText1 = "不可能出现的字符串",displayedText2 = "不可能出现的字符串2";
    	String totalText;
    	returnMessage = "";
    	if(!oprType.equals("")) //如果传进来的value【3】中有赋值，表示要取消设置页面
    	{
    		driver.tap(1, width/2, height/2, 500); //轻点屏幕中央
    	}
    	else //如果传进来的value[3]中为空，点击屏幕中央直至出现设置页面
    	{
	    	if(appType.equalsIgnoreCase("ZS")) //获取弹出设置洁面后页面上应该能显示的字段
			{
	    		displayedText = "目录";
	    		displayedText1 = "退出朗读"; //对于追书来说，有可能弹出页面显示的信息不一样，所以不同的情况都要考虑
	    		displayedText2 = "减速 -";
			}
	    	else if(appType.equalsIgnoreCase("MHD"))
			{
				displayedText = "目录";
			}
			else if(appType.equalsIgnoreCase("KJ"))
			{
				displayedText = "下一章";
				displayedText1 = "退出朗读"; //对于开卷来说，有可能弹出页面显示的信息不一样，所以不同的情况都要考虑
				displayedText2 = "放 弃";
			}
			else
			{
				System.out.print("未设定app类型");
			}
	    	try{
	    		driver.tap(1, width/2, height/2, 500); //轻点屏幕中央
		    	totalText=driver.getPageSource();  
			}
			catch(Exception e)
			{
				totalText=driver.getPageSource();  
			}

	    	
			while(!setDisplayed && counter <6){ //如果弹不出来，最多弹6遍
				if(totalText.contains(displayedText) || totalText.contains(displayedText1)||totalText.contains(displayedText2))
				{
				   setDisplayed = true;
				   break;
				}
				else
				{
			        try{
			        	Thread.sleep(3000);
			    		driver.tap(1, width/2, height/2, 500); //轻点屏幕中央
						totalText=driver.getPageSource();
					}
					catch(Exception e)
					{
						totalText=driver.getPageSource();  
					}
					counter = counter +1;
			   }
			}
			if(counter == 6)
			{
				returnMessage = "未能弹出设置界面";
			}
    	}

    }
    public String getReturnMessage()
    {
    	return returnMessage;
    }
}