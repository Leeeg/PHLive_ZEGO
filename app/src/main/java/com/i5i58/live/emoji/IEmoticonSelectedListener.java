package com.i5i58.live.emoji;

public interface IEmoticonSelectedListener {
	void onEmojiSelected(String key);

	void onStickerSelected(String categoryName, String stickerName);
}
