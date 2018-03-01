package com.i5i58.live.emoji;

import java.util.List;

public class MyRichText {

	private String text;
	
	private List<MySpan> spans;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public List<MySpan> getSpans() {
		return spans;
	}

	public void setSpans(List<MySpan> spans) {
		this.spans = spans;
	}
	
	
}
