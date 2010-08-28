package ttwapi.common;


import org.apache.commons.lang.StringEscapeUtils;
import org.apache.oro.text.perl.Perl5Util;

import com.google.appengine.repackaged.com.google.common.util.Base64;

public class TweetParser {

	public static String parseText(String text) {
		Perl5Util perl = new Perl5Util();
		String text_e = StringEscapeUtils.escapeHtml(text);
		String temp;

		String url_reg = "s/\\b([a-zA-Z]+:\\/\\/[\\w_.\\-]+\\.[a-zA-Z]{2,6}[\\/\\w\\-~.?=&%#+$*!:;]*)\\b/<a href=\"$1\" class=\"twitter-link\" class=\"web_link\" target=\"_blank\">$1<\\/a>/ig";
		String mail_reg = "s/\\b([a-zA-Z][a-zA-Z0-9\\_\\.\\-]*[a-zA-Z]*\\@[a-zA-Z][a-zA-Z0-9\\_\\.\\-]*[a-zA-Z]{2,6})\\b/<a href=\"mailto:$1\" class=\"web_link\" >$1<\\/a>/ig";
		String user_reg = "s/([\\s|\\.|\\,|\\:|\\xA1|\\xBF\\>|\\{|\\(]?)@{1}(\\w*)([\\.|\\,|\\:|\\!|\\?|\\>|\\}|\\)]?)([\\s]|$)/$1\\<a href=\"\\/user\\?id=$2\" class=\"user_link\"\\>@$2\\<\\/a\\>$3 /ig";
		String trend_reg = "s/([\\s|\\.|\\,|\\:|\\xA1|\\xBF\\>|\\{|\\(]?)#{1}(\\w*)([\\.|\\,|\\:|\\!|\\?|\\>|\\}|\\)]?)([\\s]|$)/$1\\<a href=\"\\/search\\?s=%23$2\" class=\"search_link\"\\>#$2\\<\\/a\\>$3 /ig";
		String shorturl_reg = "m/(href=\\\"http:\\/\\/(bit.ly|j.mp|ff.im)\\/[\\w\\-]{3,10})\\\"/i";

		String rst = perl.substitute(url_reg, text_e);
		rst = perl.substitute(mail_reg, rst);
		rst = perl.substitute(user_reg, rst);
		rst = perl.substitute(trend_reg, rst);

		temp = rst;
		while (perl.match(shorturl_reg, temp)) {
			rst = rst.replace(perl.group(0), "href=\"/expend?u=" + Base64.encode(Base64.encode(perl.group(0).substring(6, perl.group(0).length() - 1).getBytes()).getBytes()) + "\"");
			temp = perl.postMatch();
		}

		temp = text_e;

		rst = "<div class=\"twittertext\">" + rst + "</div>";

		return rst;
	}

}
