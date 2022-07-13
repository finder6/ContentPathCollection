package burp;

import me.jfishing.burp.dict.content_path.ContentPathDict;
import me.jfishing.burp.dict.content_path.ContentPathParser;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class BurpExtender implements IScannerCheck,IBurpExtender {

    private IBurpExtenderCallbacks callbacks;
    private IExtensionHelpers helpers;
    private PrintWriter stdout;
    private PrintWriter stderr;
    private ContentPathDict contentPathDict;
    private String lastContentPath = "";
    private String[] staticSuffix = {".js",".gif",".jpg",".png",".css",".json",".woff",".bmp"};

    public void registerExtenderCallbacks(IBurpExtenderCallbacks callbacks) {
        this.callbacks = callbacks;
        this.callbacks.setExtensionName("DictCollection");//插件名字
        this.callbacks.setProxyInterceptionEnabled(false);
        callbacks.registerScannerCheck(this);
        helpers = callbacks.getHelpers();
        stdout = new PrintWriter(callbacks.getStdout(), true);
        stderr = new PrintWriter(callbacks.getStderr(), true);
        // 字典初始化
        try {
            contentPathDict = new ContentPathDict();
        } catch (IOException e) {
            e.printStackTrace(stderr);
        }
        stdout.println("DictCollection Plugin is working");
    }

    public List<IScanIssue> doPassiveScan(IHttpRequestResponse baseRequestResponse) {
        IRequestInfo requestInfo = helpers.analyzeRequest(baseRequestResponse);
        IResponseInfo responseInfo = helpers.analyzeResponse(baseRequestResponse.getResponse());
        // 只要get请求
        if(!"GET".equals(requestInfo.getMethod())) {
            return null;
        }
        // 过滤200-299以外的状态
        if(responseInfo.getStatusCode() < 200 || responseInfo.getStatusCode() >= 300) {
            return null;
        }

        // 获取contentpath和uri
        String requestPath = requestInfo.getUrl().getPath();
        String contentPath = ContentPathParser.parse(requestPath);

        // 已经存在
        if(contentPath == null || lastContentPath.equals(contentPath) || contentPathDict.hasValue(contentPath)) {
            return null;
        }
        try {
            lastContentPath = contentPath;
            // 后缀过滤 js,gif,jpg,png,css,json,woff,bmp
            for(String suffix : staticSuffix) {
                if(contentPath.endsWith(suffix)) {
                    return null;
                }
            }
            contentPathDict.addDict(contentPath);
            stdout.println(contentPath + " add success");
        } catch (IOException e) {
            e.printStackTrace(stderr);
        }


        return null;
    }

    public List<IScanIssue> doActiveScan(IHttpRequestResponse baseRequestResponse, IScannerInsertionPoint insertionPoint) {
        return null;
    }

    public int consolidateDuplicateIssues(IScanIssue existingIssue, IScanIssue newIssue) {
        return 0;
    }
}
