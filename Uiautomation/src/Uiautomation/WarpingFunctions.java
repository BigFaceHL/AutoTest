package Uiautomation;

import java.awt.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;


import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Verify;

import io.appium.java_client.android.AndroidDriver;

import org.openqa.selenium.firefox.FirefoxProfile;

public class WarpingFunctions {
    public static boolean ever_True = false; // 用于在对if-else句式进行解析时，记录之前是否已经有过条件被满足，特定步骤被执行的情况

    public static FirefoxProfile downloadAttachments() {
        String downloadFilePath = "D:\\download";
        FirefoxProfile profile = new FirefoxProfile();
        profile.setPreference("browser.download.folderList", 2);
        profile.setPreference("browser.download.manager.showWhenStarting", false);
        profile.setPreference("browser.download.dir", downloadFilePath);
        profile.setPreference("browser.helperApps.neverAsk.openFile", "application/docx");
        profile.setPreference("browser.helperApps,neverAsk.openFile", "application/pdf");
        profile.setPreference("browser.helperApps,neverAsk.saveToDisk", "application/docx");
        profile.setPreference("browser.helperApps,neverAsk.saveToDisk", "application/pdf");
        profile.setPreference("browser.helperApps.alwaysAsk.force", false);
        profile.setPreference("browser.download.manager.alertOnEXEOpen", false);
        profile.setPreference("browser.download.manager.focusWhenStarting", false);
        profile.setPreference("browser.download.manager.useWindow", false);
        profile.setPreference("browser.download.manager.showAlertOnComplete", false);
        profile.setPreference("browser.download.manager.closeWhenDone", false);
        return profile;
    }

    public static void swithToNewWindows() {
        WebDriver driver;
        driver = new FirefoxDriver();
        Set<String> handle = driver.getWindowHandles();
        int i = 0;
        for (String handleContent : handle) {
            if (i == 1) {
                driver.switchTo().window(handleContent);
            }
            System.out.println(handleContent);
            i++;
        }
    }

    public static String verifyTest(String a, String b) {

        String result = "";

        if (a.equals(b)) {
            result = "pass";
        } else {
            result = "fail";
            System.out.println("check failed");
        }
        return result;
    }

    public static String verifyContainTest(String pageSourceString, String expectedValue, String checkPoint) {

        String result = "";
        // 如果要校验存在的文字实际存在，或者校验要不存在的文字实际不存在，校验结果正确；否则不正确
        if ((pageSourceString.contains(expectedValue) && checkPoint.equals("y")) || (!(pageSourceString.contains(expectedValue)) && checkPoint.equals("n"))) {
            result = "pass";
        } else {
            Pattern pNum = Pattern.compile("[0-9]");
            Pattern pNotNum = Pattern.compile("[^0-9]");

            Matcher expctedMacther = pNum.matcher(expectedValue);
            Matcher pagesourceMacther = pNum.matcher(pageSourceString);

            if (!pagesourceMacther.find() || !expctedMacther.find()) {
                System.out.println("check failed");
                result = "fail";
                return result;
            }

            expctedMacther = pNotNum.matcher(expectedValue);
            pagesourceMacther = pNotNum.matcher(pageSourceString);

            if (pagesourceMacther.replaceAll("").trim().contains(expctedMacther.replaceAll("").trim())) {
                result = "pass";
                return result;
            }
            result = "fail";
        }
        return result;
    }

    // 从一个大的字符串中，根据2个关键字，截取其中间的内容
    public static String getFiltedText(String bigText, String filterString) {

        String result = "";
        String Str1 = "", Str2 = "";
        int beginIdx, endIdx, i, j;
        String[] strs;
        if (bigText.contains("/")) {
            strs = filterString.split("\\*");// 两个关键字以"*"隔开
            i = filterString.indexOf("\\*");
            j = filterString.length() - 1;
        } else {
            strs = filterString.split("/");// 两个关键字以"/"隔开
            i = filterString.indexOf('/');
            j = filterString.length() - 1;
        }

        Str1 = strs[0];

        if (i != j) // 如果"/"在最后一个字符的位置，给Str2置为空
        {
            Str2 = strs[1];
        }

        if (Str1.length() == 0) // 如果第一个关键字为空，从第一个字符开始截取
        {
            beginIdx = 0;
            endIdx = bigText.indexOf(Str2);
        } else if (Str2.length() == 0) // 如果第二个关机字为空，截取到最后一个字符
        {
            beginIdx = bigText.indexOf(Str1) + Str1.length();
            endIdx = bigText.length();
        } else // 如果2个关机字都不为空，截取器中间的内容
        {
            beginIdx = bigText.indexOf(Str1) + Str1.length();
            endIdx = bigText.indexOf(Str2);
            if (beginIdx >= endIdx) // 如果起始位置大于结束为止，说明要查找的字符串出现了2次，取第二次出现的位置
            {
                endIdx = bigText.indexOf(Str2, beginIdx);
            }
        }

        result = bigText.substring(beginIdx, endIdx);

        return result.toString();
    }

    public static String comment(int a, int b) {
        String result = "";
        if (a == b) {
            result = "pass";
        } else {
            result = "fail";
            System.out.println("check failed");
        }
        return result;
    }

    static Map<String, Boolean> ifMap = new HashMap<String, Boolean>();


    // 判断该步骤是否需要执行。根据oprType与字段containedValue的关系，确定后续用例的步骤要否执行
    public static boolean getIfCaseExec(AndroidDriver driver, String oprType, String containedValue, boolean orignalResult, String isMulti, String elementValue, String key) {
        boolean stepExec = orignalResult; // 默认情况系下，所有用例都执行
        String totalText;
        String parentKey = null;
        String ifKey = null;

        if ((oprType.contains("if_") && !oprType.contains("else_if")) || (oprType.contains("IF_") && !oprType.contains("ELSE_IF"))) {
            // 如果value[0]中有文本包含的字样:
            // (1)若页面中包含字段containedValue，执行后续的用例步骤都，直到出现新的逻辑判断步骤包含在value【0】中
            // (2)若页面中不包含字段containedValue，则后续的用例步骤都不执行；直到出现新的逻辑判断步骤包含在value【0】中
            if (key != null && !"".equals(key)) {
                parentKey = "if" + (Integer.parseInt(key) - 1);
                ifKey = "if" + (Integer.parseInt(key));
                if (Integer.parseInt(key) - 1 > 0) {
                    if (!ifMap.get(parentKey)) {
                        stepExec = false;
                        return stepExec;
                    }
                }
            }

            if (oprType.contains("文本包含")) {
                try {
                    totalText = driver.getPageSource();
                    int k = 0;
                    while (k < 3) {
                        if (totalText.contains(containedValue)) {
                            stepExec = true;
                            ever_True = true;
                            System.out.println("页面包括该内容：" + containedValue);
                            break;
                        } else {
                            try {
                                Thread.sleep(3000);
                            } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            totalText = driver.getPageSource();
                            k = k + 1;
                        }
                    }
                    if (k == 3) {
                        stepExec = false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(e.getMessage());
                }


            }
            // 如果value[0]中有文本不包含的字样:
            // (1)若页面中不包含字段containedValue，执行后续的用例步骤都，直到出现新的逻辑判断步骤包含在value【0】中
            // (2)若页面中包含字段containedValue，则后续的用例步骤都不执行；直到出现新的逻辑判断步骤包含在value【0】中
            else if (oprType.contains("文本不包含")) {
                totalText = driver.getPageSource();
                int j = 0;
                while (j < 3) {
                    if ((totalText.contains(containedValue))) {
                        stepExec = false;
                        break;
                    } else {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        totalText = driver.getPageSource();
                        j = j + 1;
                    }
                }
                if (j == 3) {
                    System.out.println("页面不包括该内容：" + containedValue);
                    stepExec = true;
                    ever_True = true;
                }
            } else if (oprType.contains("页面包含xpath")) {

                try {
                    WebDriverWait wait = new WebDriverWait(driver, 15);// 最多等待时间由maxWaitTime指定

                    if (isMulti.equals("")) {
                        if (wait.until(ExpectedConditions.elementToBeClickable(By.xpath(elementValue))) != null) {
                            stepExec = true;
                            ever_True = true;
                            System.out.println("页面包括该内容：" + containedValue);
                        } else {
                            stepExec = false;
                            System.out.println("页面包不括该内容：" + containedValue);
                        }

                    } else {
                        if (wait.until(ExpectedConditions.elementToBeClickable((WebElement) driver.findElements(By.xpath(elementValue)))) != null) {
                            stepExec = true;
                            ever_True = true;
                            System.out.println("页面包括该内容：" + containedValue);
                        } else {
                            stepExec = false;
                            System.out.println("页面包不括该内容：" + containedValue);
                        }
                    }

                } catch (Exception e) {
                    stepExec = false;
                    System.out.println("页面包不括该内容：" + containedValue);
                }
            } else if (oprType.contains("页面包含id")) {

                try {
                    WebDriverWait wait = new WebDriverWait(driver, 10);// 最多等待时间由maxWaitTime指定

                    if (isMulti.equals("")) {
                        if (wait.until(ExpectedConditions.elementToBeClickable(By.id(elementValue))) != null) {
                            stepExec = true;
                            ever_True = true;
                            System.out.println("页面包括该内容：" + containedValue);
                        } else {
                            stepExec = false;
                            System.out.println("页面包bu括该内容：" + containedValue);
                        }

                    } else {
                        if (wait.until(ExpectedConditions.elementToBeClickable((WebElement) driver.findElements(By.id(elementValue)))) != null) {
                            stepExec = true;
                            ever_True = true;
                            System.out.println("页面包括该内容：" + containedValue);
                        } else {
                            stepExec = false;
                            System.out.println("页面包bu括该内容：" + containedValue);
                        }
                    }

                } catch (Exception e) {
                    stepExec = false;

                    System.out.println("页面包不括该内容：" + containedValue);
                }
            }

            if (key != null && !"".equals(key)) {
                ifMap.put(ifKey, stepExec);
            }

        } else if (oprType.contains("else_if") || oprType.contains("ELSE_IF")) {
            // 如果之前已有步骤被执行，则else_if后续的步骤不再执行；
            if (ever_True == true) {
                stepExec = false;
            }
            // 否则，如果value[0]中有文本包含的字样:
            // (1)若页面中包含字段containedValue，执行后续的用例步骤都，直到出现新的逻辑判断步骤包含在value【0】中
            // (2)若页面中不包含字段containedValue，则后续的用例步骤都不执行；直到出现新的逻辑判断步骤包含在value【0】中
            else if (oprType.contains("文本包含")) {
                totalText = driver.getPageSource();
                int k = 0;
                while (k < 3) {
                    if (totalText.contains(containedValue)) {
                        stepExec = true;
                        ever_True = true;
                        break;
                    } else {
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        totalText = driver.getPageSource();
                        k = k + 1;
                    }
                }
                if (k == 3) {
                    stepExec = false;
                }
            }
            // 否则，如果value[0]中有文本不包含的字样:
            // (1)若页面中不包含字段containedValue，执行后续的用例步骤都，直到出现新的逻辑判断步骤包含在value【0】中
            // (2)若页面中包含字段containedValue，则后续的用例步骤都不执行；直到出现新的逻辑判断步骤包含在value【0】中
            else if (oprType.contains("文本不包含")) {
                totalText = driver.getPageSource();
                int j = 0;
                while (j < 3) {
                    if ((totalText.contains(containedValue))) {
                        stepExec = false;
                        break;
                    } else {
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        totalText = driver.getPageSource();
                        j = j + 1;
                    }
                }
                if (j == 3) {
                    stepExec = true;
                    ever_True = true;
                }
            }
            //搜集执行结果
            if (key != null && !"".equals(key)) {
                ifMap.put(key, stepExec);
            }
        }
        // 若value[0]为else，则如果之前的步骤不执行，之后的步骤执行；反之则相反；
        else if (oprType.equalsIgnoreCase("else")) {

            if (key != null && !"".equals(key)) {
                parentKey = "if" + (Integer.parseInt(key) - 1);
                ifKey = "if" + (Integer.parseInt(key));

                //判断是否属于内-判断执行结果
                if (Integer.parseInt(key) - 1 > 0) {
                    if (!ifMap.get(parentKey)) {
                        stepExec = false;
                        return stepExec;
                    }
                }
                ever_True = ifMap.get(ifKey);
            }
            if (ever_True == true) {
                stepExec = false;
            } else {
                stepExec = true;
            }


        }
        // 若value【0】为end，后续所有的步骤都执行
        else if (oprType.equalsIgnoreCase("end")) {
            stepExec = true;
            ever_True = false;
        } else // 若value【0】为一般的步骤，stepExec值不变
        {
            stepExec = orignalResult;
        }
        return stepExec;
    }

    // 如果QQ因为签名问题未能登录成功，持续登录直至成功
    public static String contQQLogin(AndroidDriver driver) {
        String retMessage = "";
        driver.findElement(By.name("确定")).click();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        driver.findElement(By.name("登录")).click();
        return retMessage;
    }
}
