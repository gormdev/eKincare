package com.ekincare.util;


import com.ekincare.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.widget.TextView;

public class AvenirTextView extends TextView{
	   /*
	 * Permissible values ​​for the "typeface" attribute.
	 */
	private final static int AVENIR_BLACK = 0;
	private final static int AVENIR_BLACK_OBLIQUE = 1;
	private final static int AVENIR_BOOK = 2;
	private final static int AVENIR_BOOK_OBLIQUE = 3;
	private final static int AVENIR_HEAVY = 4;
	private final static int AVENIR_HEAVY_OBLIQUE = 5;
	private final static int AVENIR_LIGHT = 6;
	private final static int AVENIR_LIGHT_OBLIQUE = 7;
	private final static int AVENIR_MEDIUM = 8;
	private final static int AVENIR_MEDIUM_OBLIQUE = 9;
	private final static int AVENIR_OBLIQUE = 10;
	private final static int AVENIR_ROMAN = 11;

	/**
     * List of created typefaces for later reused.
     */
    private final static SparseArray<Typeface> mTypefaces = new SparseArray<Typeface>(12);


	public AvenirTextView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Constructor that is called when inflating a view from XML. This is called
	 * when a view is being constructed from an XML file, supplying attributes
	 * that were specified in the XML file. This version uses a default style of
	 * 0, so the only attribute values applied are those in the Context's Theme
	 * and the given AttributeSet.
	 * <p/>
	 * <p/>
	 * The method onFinishInflate() will be called after all children have been
	 * added.
	 *
	 * @param context
	 *            The Context the view is running in, through which it can
	 *            access the current theme, resources, etc.
	 * @param attrs
	 *            The attributes of the XML tag that is inflating the view.
	 * @see #RobotoTextView(Context, AttributeSet, int)
	 */
	public AvenirTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		parseAttributes(context, attrs);
	}

	/**
	 * Parse the attributes.
	 *
	 * @param context
	 *            The Context the view is running in, through which it can
	 *            access the current theme, resources, etc.
	 * @param attrs
	 *            The attributes of the XML tag that is inflating the view.
	 */
	private void parseAttributes(Context context, AttributeSet attrs) {
		TypedArray values = context.obtainStyledAttributes(attrs,
				R.styleable.AvenirTextView);

		int typefaceValue = values.getInt(R.styleable.AvenirTextView_avenirtypeface,0);
		values.recycle();

		setTypeface(obtaintTypeface(context, typefaceValue));
	}
	/**
	 * Obtain typeface.
	 *
	 * @param context
	 *            The Context the view is running in, through which it can
	 *            access the current theme, resources, etc.
	 * @param typefaceValue
	 *            values ​​for the "typeface" attribute
	 * @return Roboto {@link Typeface}
	 * @throws IllegalArgumentException
	 *             if unknown `typeface` attribute value.
	 */
	private Typeface obtaintTypeface(Context context, int typefaceValue)
			throws IllegalArgumentException {
		Typeface typeface = mTypefaces.get(typefaceValue);
		if (typeface == null) {
			typeface = createTypeface(context, typefaceValue);
			mTypefaces.put(typefaceValue, typeface);
		}
		return typeface;
	}
	 /**
     * Create typeface from assets.
     *
     * @param context       The Context the view is running in, through which it can
     *                      access the current theme, resources, etc.
     * @param typefaceValue values ​​for the "typeface" attribute
     * @return Roboto {@link Typeface}
     * @throws IllegalArgumentException if unknown `typeface` attribute value.
     */
    private Typeface createTypeface(Context context, int typefaceValue) throws IllegalArgumentException {
        Typeface typeface;
        switch (typefaceValue) {
            case AVENIR_BLACK:
                typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Avenir-Black.ttf");
                break;
            case AVENIR_BLACK_OBLIQUE:
                typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Avenir-BlackOblique.ttf");
                break;
            case AVENIR_BOOK:
                typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Avenir-Book.ttf");
                break;
            case AVENIR_BOOK_OBLIQUE:
                typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Avenir-BookOblique.ttf");
                break;
            case AVENIR_HEAVY:
                typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Avenir-Heavy.ttf");
                break;
            case AVENIR_HEAVY_OBLIQUE:
                typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Avenir-HeavyOblique.ttf");
                break;
            case AVENIR_LIGHT:
                typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Avenir-Light.ttf");
                break;
            case AVENIR_LIGHT_OBLIQUE:
                typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Avenir-LightOblique.ttf");
                break;
            case AVENIR_MEDIUM:
                typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Avenir-Medium.ttf");
                break;
            case AVENIR_MEDIUM_OBLIQUE:
                typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Avenir-MediumOblique.ttf");
                break;
            case AVENIR_OBLIQUE:
                typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Avenir-Oblique.ttf");
                break;
            case AVENIR_ROMAN:
                typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Avenir-Roman.ttf");
                break;
            default:
                throw new IllegalArgumentException("Unknown `typeface` attribute value " + typefaceValue);
        }
        return typeface;
    }

}
