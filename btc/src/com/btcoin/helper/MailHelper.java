package com.btcoin.helper;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;

public class MailHelper {
	private MimeMessage mimeMsg; // MIME邮件对象
	private Session session; // 邮件会话对象
	private Properties props; // 系统属性
	private static final String smtp = "smtp.163.com";
	private static final String username = "extranotgood2@163.com"; // smtp认证用户名和密码
	private static final String password = "abc123";
	private int port = 25;
	private Multipart mp; // Multipart对象,邮件内容,标题,附件等内容均添加到其中后再生成MimeMessage对象
	
	private static final String domain = "localhost:8090";
	
	private static Logger logger = Logger.getLogger(MailHelper.class.getName());

	public MailHelper(String smtp) {
		setSmtpHost(smtp);
		createMimeMessage();
	}

	public void setSmtpHost(String hostName) {
		if (props == null)
			props = System.getProperties(); // 获得系统属性对象
		props.put("mail.smtp.host", hostName); // 设置SMTP主机
		props.put("mail.smtp.port", port);
	}

	// 创建MIME邮件对象
	public boolean createMimeMessage() {
		try {
			session = Session.getDefaultInstance(props, null); // 获得邮件会话对象
			mimeMsg = new MimeMessage(session); // 创建MIME邮件对象
			mp = new MimeMultipart();
			return true;
		} catch (Exception e) {
			logger.debug("创建MIME邮件对象失败！" + e);
			return false;
		}
	}

	// 是否需要验证
	public void setNeedAuth(boolean need) {
		if (props == null)
			props = System.getProperties();
		if (need) {
			props.put("mail.smtp.auth", "true");
		} else {
			props.put("mail.smtp.auth", "false");
		}
	}

	// 设置主题
	public boolean setSubject(String mailSubject) {
		try {
			mimeMsg.setSubject(mailSubject);
			return true;
		} catch (Exception e) {
			logger.debug("设置邮件主题发生错误！");
			return false;
		}
	}

	// 设置邮件主体
	public boolean setBody(String mailBody) {
		try {
			BodyPart bp = new MimeBodyPart();
			bp.setContent(
					"<meta http-equiv=Content-Type content=text/html; charset=gb2312>"
							+ mailBody, "text/html;charset=GB2312");
			mp.addBodyPart(bp);
			return true;
		} catch (Exception e) {
			logger.debug("设置邮件正文时发生错误！" + e);
			return false;
		}
	}

	// 设置发信人
	public boolean setFrom(String from) {
		try {
			mimeMsg.setFrom(new InternetAddress(from));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	// 设置收信人
	public boolean setTo(String to) {
		if (to == null)
			return false;
		try {
			mimeMsg.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(to));
			logger.debug("收信人 is " + to);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	// 复制邮件到收件人
	public boolean setCopyTo(String copyto) {
		if (copyto == null)
			return false;
		try {
			mimeMsg.setRecipients(Message.RecipientType.CC,
					(Address[]) InternetAddress.parse(copyto));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	// 发送邮件
	public boolean sendout() {
		try {
			mimeMsg.setContent(mp);
			mimeMsg.saveChanges();

			Session mailSession = Session.getInstance(props, null);
			mailSession.setDebug(false);
			Transport transport = mailSession.getTransport("smtp");
			transport.connect((String) props.get("mail.smtp.host"), username, password);
			transport.sendMessage(mimeMsg, mimeMsg.getRecipients(Message.RecipientType.TO));
			transport.close();
			return true;
		} catch (Exception e) {
			//邮件发送失败预警
			logger.error(e.getMessage());
			return false;
		}
	}

	private static boolean fromMail(MailInfo mailInfo) {
		MailHelper themail = new MailHelper(smtp);
		themail.setNeedAuth(true);// 是否要验证

		themail.setSubject(mailInfo.getSubject());// 主题
		String mailbody = mailInfo.getMailbody();// 邮件内容也是激活用户的链接
		themail.setBody(mailbody);
		themail.setFrom(username);// 发送者
		themail.setTo(mailInfo.getToMail());// 接收者
		
		return themail.sendout();
	}
	
	private static MailInfo getResetPwdEmailInfo(String email, String username, String link, String lang) {
		MailInfo mailInfo = new MailInfo();
		mailInfo.setToMail(email);
		
		StringBuilder sb = new StringBuilder();
		if ("zh_cn".equals(lang.toLowerCase())) {
			sb.append("<pre style=\"word-wrap:break-word;\" wrap=\"soft\">");
			sb.append("<br/>");
			sb.append("本邮件是应您在<a href=\"http://"+domain+"\">http://"+domain+"</a>上提交的重置密码请求，而给您发过来的验证邮件。");
			sb.append("<br/><br/>");
			sb.append("如果您没有提交重置密码请求而收到此邮件，我们非常抱歉打扰您，请忽略本邮件。");
			sb.append("<br/><br/>");
			sb.append("要重置您的用户密码，请点击以下链接。该链接会在浏览器上打开一个页面，让您来重设密码。");
			sb.append("<br/>");
			sb.append("<a target=\"_blank\" href=\"http://" + domain + link + "\">http://" + domain + link + "</a>");
			sb.append("<br/><br/>");
			sb.append("您当前邮箱关联的用户名为: " + username);
			sb.append("<br/><br/>");
			sb.append("上述重设密码链接，在您发出重置密码请求后一小时内有效。感谢您浏览我们的网站！");
			sb.append("<br/><br/>");
			sb.append("xxxx 开发团队");
			sb.append("</pre>");
			
			mailInfo.setSubject("重置您的密码");
		} else {
			sb.append("<pre style=\"word-wrap:break-word;\" wrap=\"soft\">");
			sb.append("<br/>");
			sb.append("You're receiving this e-mail because you requested a password reset for your user account at " + domain);
			sb.append("<br/><br/>");
			sb.append("Please go to the following page and choose a new password:");
			sb.append("<br/><br/>");
			sb.append("<a target=\"_blank\" href=\"http://" + domain + link + "\">http://" + domain + link + "</a>");
			sb.append("<br/><br/>");
			sb.append("Your username, in case you've forgotten: " + username);
			sb.append("<br/><br/>");
			sb.append("Thanks for using our site!");
			sb.append("<br/><br/>");
			sb.append("The xxxx Team");
			sb.append("</pre>");
			
			mailInfo.setSubject("Password reset on " + domain);
		}
		mailInfo.setMailbody(sb.toString());
		
		return mailInfo;
	}
	private static MailInfo getRegisterRebutEmailInfo(String email, String username, String reasons, String lang) {
		MailInfo mailInfo = new MailInfo();
		mailInfo.setToMail(email);
		
		StringBuilder sb = new StringBuilder();
		if ("zh_cn".equals(lang.toLowerCase())) {
			sb.append("亲爱的用户：经过我们对你提供的资料审核不通过,理由如下:"+reasons);
			mailInfo.setSubject("注册用户");
		} else {
			sb.append("亲爱的用户：经过我们对你提供的资料审核不通过,很抱歉!"+reasons);
			mailInfo.setSubject("注册用户");
		}
		mailInfo.setMailbody(sb.toString());
		
		return mailInfo;
	}
	
	public static boolean sendResetPwdEmail(String email, String username, String link, String lang) {
		MailInfo mailInfo = getResetPwdEmailInfo(email, username, link, lang);
		
		return fromMail(mailInfo);
	}
	public static boolean sendRegisterRebutEmail(String email, String username, String reasons, String lang) {
		MailInfo mailInfo = getRegisterRebutEmailInfo(email, username, reasons, lang);
		
		return fromMail(mailInfo);
	}
}
