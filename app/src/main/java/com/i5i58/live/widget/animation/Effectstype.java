package com.i5i58.live.widget.animation;

/**
  * 绫诲悕绉�:   Effectstype 
  * 鍒涘缓浜�:    xhl 
  * 鍒涘缓鏃堕棿:  2014-12-24 涓嬪崍2:16:20     
  * 鐗堟湰:      v1.0  
  * 绫绘弿杩�:	NiftyDialogBuilder鐨勫姩鐢绘晥鏋滄灇涓剧被
 */
public enum Effectstype {

    Fadein(FadeIn.class),
    Slideleft(SlideLeft.class),
    Slidetop(SlideTop.class),
    SlideBottom(SlideBottom.class),
    Slideright(SlideRight.class),
    Fall(Fall.class),
    Newspager(NewsPaper.class),
    Fliph(FlipH.class),
    Flipv(FlipV.class),
    RotateBottom(RotateBottom.class),
    RotateLeft(RotateLeft.class),
    Slit(Slit.class),
    Shake(Shake.class),
    Sidefill(SideFall.class);
    private Class<? extends BaseEffects> effectsClazz;

    private Effectstype(Class<? extends BaseEffects> mclass) {
        effectsClazz = mclass;
    }

    public BaseEffects getAnimator() {
        BaseEffects bEffects=null;
	try {
		bEffects = effectsClazz.newInstance();
	} catch (ClassCastException e) {
		throw new Error("Can not init animatorClazz instance");
	} catch (InstantiationException e) {
		throw new Error("Can not init animatorClazz instance");
	} catch (IllegalAccessException e) {
		throw new Error("Can not init animatorClazz instance");
	}
	return bEffects;
    }
}
