package com.pwc.core.framework.processors.mobile;

import com.pwc.core.framework.processors.mobile.elements.XCUIElementTypeActivityIndicatorImpl;
import com.pwc.core.framework.processors.mobile.elements.XCUIElementTypeAlertImpl;
import com.pwc.core.framework.processors.mobile.elements.XCUIElementTypeAnyImpl;
import com.pwc.core.framework.processors.mobile.elements.XCUIElementTypeApplicationImpl;
import com.pwc.core.framework.processors.mobile.elements.XCUIElementTypeBrowserImpl;
import com.pwc.core.framework.processors.mobile.elements.XCUIElementTypeButtonImpl;
import com.pwc.core.framework.processors.mobile.elements.XCUIElementTypeCellImpl;
import com.pwc.core.framework.processors.mobile.elements.XCUIElementTypeCheckBoxImpl;
import com.pwc.core.framework.processors.mobile.elements.XCUIElementTypeCollectionViewImpl;
import com.pwc.core.framework.processors.mobile.elements.XCUIElementTypeColorWellImpl;
import com.pwc.core.framework.processors.mobile.elements.XCUIElementTypeComboBoxImpl;
import com.pwc.core.framework.processors.mobile.elements.XCUIElementTypeDatePickerImpl;
import com.pwc.core.framework.processors.mobile.elements.XCUIElementTypeDecrementArrowImpl;
import com.pwc.core.framework.processors.mobile.elements.XCUIElementTypeDialogImpl;
import com.pwc.core.framework.processors.mobile.elements.XCUIElementTypeDisclosureTriangleImpl;
import com.pwc.core.framework.processors.mobile.elements.XCUIElementTypeDockItemImpl;
import com.pwc.core.framework.processors.mobile.elements.XCUIElementTypeDrawerImpl;
import com.pwc.core.framework.processors.mobile.elements.XCUIElementTypeGridImpl;
import com.pwc.core.framework.processors.mobile.elements.XCUIElementTypeGroupImpl;
import com.pwc.core.framework.processors.mobile.elements.XCUIElementTypeHandleImpl;
import com.pwc.core.framework.processors.mobile.elements.XCUIElementTypeHelpTagImpl;
import com.pwc.core.framework.processors.mobile.elements.XCUIElementTypeIconImpl;
import com.pwc.core.framework.processors.mobile.elements.XCUIElementTypeImageImpl;
import com.pwc.core.framework.processors.mobile.elements.XCUIElementTypeIncrementArrowImpl;
import com.pwc.core.framework.processors.mobile.elements.XCUIElementTypeKeyImpl;
import com.pwc.core.framework.processors.mobile.elements.XCUIElementTypeKeyboardImpl;
import com.pwc.core.framework.processors.mobile.elements.XCUIElementTypeLayoutAreaImpl;
import com.pwc.core.framework.processors.mobile.elements.XCUIElementTypeLayoutItemImpl;
import com.pwc.core.framework.processors.mobile.elements.XCUIElementTypeLevelIndicatorImpl;
import com.pwc.core.framework.processors.mobile.elements.XCUIElementTypeLinkImpl;
import com.pwc.core.framework.processors.mobile.elements.XCUIElementTypeMapImpl;
import com.pwc.core.framework.processors.mobile.elements.XCUIElementTypeMatteImpl;
import com.pwc.core.framework.processors.mobile.elements.XCUIElementTypeMenuBarImpl;
import com.pwc.core.framework.processors.mobile.elements.XCUIElementTypeMenuBarItemImpl;
import com.pwc.core.framework.processors.mobile.elements.XCUIElementTypeMenuButtonImpl;
import com.pwc.core.framework.processors.mobile.elements.XCUIElementTypeMenuImpl;
import com.pwc.core.framework.processors.mobile.elements.XCUIElementTypeMenuItemImpl;
import com.pwc.core.framework.processors.mobile.elements.XCUIElementTypeNavigationBarImpl;
import com.pwc.core.framework.processors.mobile.elements.XCUIElementTypeOtherImpl;
import com.pwc.core.framework.processors.mobile.elements.XCUIElementTypeOutlineImpl;
import com.pwc.core.framework.processors.mobile.elements.XCUIElementTypeOutlineRowImpl;
import com.pwc.core.framework.processors.mobile.elements.XCUIElementTypePageIndicatorImpl;
import com.pwc.core.framework.processors.mobile.elements.XCUIElementTypePickerImpl;
import com.pwc.core.framework.processors.mobile.elements.XCUIElementTypePickerWheelImpl;
import com.pwc.core.framework.processors.mobile.elements.XCUIElementTypePopUpButtonImpl;
import com.pwc.core.framework.processors.mobile.elements.XCUIElementTypePopoverImpl;
import com.pwc.core.framework.processors.mobile.elements.XCUIElementTypeProgressIndicatorImpl;
import com.pwc.core.framework.processors.mobile.elements.XCUIElementTypeRadioButtonImpl;
import com.pwc.core.framework.processors.mobile.elements.XCUIElementTypeRadioGroupImpl;
import com.pwc.core.framework.processors.mobile.elements.XCUIElementTypeRatingIndicatorImpl;
import com.pwc.core.framework.processors.mobile.elements.XCUIElementTypeRelevanceIndicatorImpl;
import com.pwc.core.framework.processors.mobile.elements.XCUIElementTypeRulerImpl;
import com.pwc.core.framework.processors.mobile.elements.XCUIElementTypeRulerMarkerImpl;
import com.pwc.core.framework.processors.mobile.elements.XCUIElementTypeScrollBarImpl;
import com.pwc.core.framework.processors.mobile.elements.XCUIElementTypeScrollViewImpl;
import com.pwc.core.framework.processors.mobile.elements.XCUIElementTypeSearchFieldImpl;
import com.pwc.core.framework.processors.mobile.elements.XCUIElementTypeSecureTextFieldImpl;
import com.pwc.core.framework.processors.mobile.elements.XCUIElementTypeSegmentedControlImpl;
import com.pwc.core.framework.processors.mobile.elements.XCUIElementTypeSheetImpl;
import com.pwc.core.framework.processors.mobile.elements.XCUIElementTypeSliderImpl;
import com.pwc.core.framework.processors.mobile.elements.XCUIElementTypeSplitGroupImpl;
import com.pwc.core.framework.processors.mobile.elements.XCUIElementTypeSplitterImpl;
import com.pwc.core.framework.processors.mobile.elements.XCUIElementTypeStaticTextImpl;
import com.pwc.core.framework.processors.mobile.elements.XCUIElementTypeStatusBarImpl;
import com.pwc.core.framework.processors.mobile.elements.XCUIElementTypeStatusItemImpl;
import com.pwc.core.framework.processors.mobile.elements.XCUIElementTypeStepperImpl;
import com.pwc.core.framework.processors.mobile.elements.XCUIElementTypeSwitchImpl;
import com.pwc.core.framework.processors.mobile.elements.XCUIElementTypeTabBarImpl;
import com.pwc.core.framework.processors.mobile.elements.XCUIElementTypeTabGroupImpl;
import com.pwc.core.framework.processors.mobile.elements.XCUIElementTypeTabImpl;
import com.pwc.core.framework.processors.mobile.elements.XCUIElementTypeTableColumnImpl;
import com.pwc.core.framework.processors.mobile.elements.XCUIElementTypeTableImpl;
import com.pwc.core.framework.processors.mobile.elements.XCUIElementTypeTableRowImpl;
import com.pwc.core.framework.processors.mobile.elements.XCUIElementTypeTextFieldImpl;
import com.pwc.core.framework.processors.mobile.elements.XCUIElementTypeTextViewImpl;
import com.pwc.core.framework.processors.mobile.elements.XCUIElementTypeTimelineImpl;
import com.pwc.core.framework.processors.mobile.elements.XCUIElementTypeToggleImpl;
import com.pwc.core.framework.processors.mobile.elements.XCUIElementTypeToolbarButtonImpl;
import com.pwc.core.framework.processors.mobile.elements.XCUIElementTypeToolbarImpl;
import com.pwc.core.framework.processors.mobile.elements.XCUIElementTypeTouchBarImpl;
import com.pwc.core.framework.processors.mobile.elements.XCUIElementTypeValueIndicatorImpl;
import com.pwc.core.framework.processors.mobile.elements.XCUIElementTypeWebViewImpl;
import com.pwc.core.framework.processors.mobile.elements.XCUIElementTypeWindowImpl;
import org.openqa.selenium.WebElement;

public class TapActivityProcessor {

    private static TapActivityProcessor instance = new TapActivityProcessor();

    private TapActivityProcessor() {
    }

    public static TapActivityProcessor getInstance() {
        return instance;
    }

    public static boolean applies(WebElement mobileElement) {
        return XCUIElementTypeCellImpl.applies(mobileElement) || XCUIElementTypeStaticTextImpl.applies(mobileElement) || XCUIElementTypeButtonImpl.applies(mobileElement);
    }

    public void mobileAction(WebElement mobileElement, Object value) {

        if (XCUIElementTypeActivityIndicatorImpl.applies(mobileElement)) {
            XCUIElementTypeActivityIndicatorImpl element = new XCUIElementTypeActivityIndicatorImpl();
            element.mobileAction(mobileElement, value);
        } else if (XCUIElementTypeAlertImpl.applies(mobileElement)) {
            XCUIElementTypeAlertImpl element = new XCUIElementTypeAlertImpl();
            element.mobileAction(mobileElement, value);
        } else if (XCUIElementTypeAnyImpl.applies(mobileElement)) {
            XCUIElementTypeAnyImpl element = new XCUIElementTypeAnyImpl();
            element.mobileAction(mobileElement, value);
        } else if (XCUIElementTypeApplicationImpl.applies(mobileElement)) {
            XCUIElementTypeApplicationImpl element = new XCUIElementTypeApplicationImpl();
            element.mobileAction(mobileElement, value);
        } else if (XCUIElementTypeBrowserImpl.applies(mobileElement)) {
            XCUIElementTypeBrowserImpl element = new XCUIElementTypeBrowserImpl();
            element.mobileAction(mobileElement, value);
        } else if (XCUIElementTypeButtonImpl.applies(mobileElement)) {
            XCUIElementTypeButtonImpl element = new XCUIElementTypeButtonImpl();
            element.mobileAction(mobileElement, value);
        } else if (XCUIElementTypeCellImpl.applies(mobileElement)) {
            XCUIElementTypeCellImpl element = new XCUIElementTypeCellImpl();
            element.mobileAction(mobileElement, value);
        } else if (XCUIElementTypeCheckBoxImpl.applies(mobileElement)) {
            XCUIElementTypeCheckBoxImpl element = new XCUIElementTypeCheckBoxImpl();
            element.mobileAction(mobileElement, value);
        } else if (XCUIElementTypeCollectionViewImpl.applies(mobileElement)) {
            XCUIElementTypeCollectionViewImpl element = new XCUIElementTypeCollectionViewImpl();
            element.mobileAction(mobileElement, value);
        } else if (XCUIElementTypeColorWellImpl.applies(mobileElement)) {
            XCUIElementTypeColorWellImpl element = new XCUIElementTypeColorWellImpl();
            element.mobileAction(mobileElement, value);
        } else if (XCUIElementTypeComboBoxImpl.applies(mobileElement)) {
            XCUIElementTypeComboBoxImpl element = new XCUIElementTypeComboBoxImpl();
            element.mobileAction(mobileElement, value);
        } else if (XCUIElementTypeDatePickerImpl.applies(mobileElement)) {
            XCUIElementTypeDatePickerImpl element = new XCUIElementTypeDatePickerImpl();
            element.mobileAction(mobileElement, value);
        } else if (XCUIElementTypeDecrementArrowImpl.applies(mobileElement)) {
            XCUIElementTypeDecrementArrowImpl element = new XCUIElementTypeDecrementArrowImpl();
            element.mobileAction(mobileElement, value);
        } else if (XCUIElementTypeDialogImpl.applies(mobileElement)) {
            XCUIElementTypeDialogImpl element = new XCUIElementTypeDialogImpl();
            element.mobileAction(mobileElement, value);
        } else if (XCUIElementTypeDisclosureTriangleImpl.applies(mobileElement)) {
            XCUIElementTypeDisclosureTriangleImpl element = new XCUIElementTypeDisclosureTriangleImpl();
            element.mobileAction(mobileElement, value);
        } else if (XCUIElementTypeDockItemImpl.applies(mobileElement)) {
            XCUIElementTypeDockItemImpl element = new XCUIElementTypeDockItemImpl();
            element.mobileAction(mobileElement, value);
        } else if (XCUIElementTypeDrawerImpl.applies(mobileElement)) {
            XCUIElementTypeDrawerImpl element = new XCUIElementTypeDrawerImpl();
            element.mobileAction(mobileElement, value);
        } else if (XCUIElementTypeGridImpl.applies(mobileElement)) {
            XCUIElementTypeGridImpl element = new XCUIElementTypeGridImpl();
            element.mobileAction(mobileElement, value);
        } else if (XCUIElementTypeGroupImpl.applies(mobileElement)) {
            XCUIElementTypeGroupImpl element = new XCUIElementTypeGroupImpl();
            element.mobileAction(mobileElement, value);
        } else if (XCUIElementTypeHandleImpl.applies(mobileElement)) {
            XCUIElementTypeHandleImpl element = new XCUIElementTypeHandleImpl();
            element.mobileAction(mobileElement, value);
        } else if (XCUIElementTypeHelpTagImpl.applies(mobileElement)) {
            XCUIElementTypeHelpTagImpl element = new XCUIElementTypeHelpTagImpl();
            element.mobileAction(mobileElement, value);
        } else if (XCUIElementTypeIconImpl.applies(mobileElement)) {
            XCUIElementTypeIconImpl element = new XCUIElementTypeIconImpl();
            element.mobileAction(mobileElement, value);
        } else if (XCUIElementTypeImageImpl.applies(mobileElement)) {
            XCUIElementTypeImageImpl element = new XCUIElementTypeImageImpl();
            element.mobileAction(mobileElement, value);
        } else if (XCUIElementTypeIncrementArrowImpl.applies(mobileElement)) {
            XCUIElementTypeIncrementArrowImpl element = new XCUIElementTypeIncrementArrowImpl();
            element.mobileAction(mobileElement, value);
        } else if (XCUIElementTypeKeyboardImpl.applies(mobileElement)) {
            XCUIElementTypeKeyboardImpl element = new XCUIElementTypeKeyboardImpl();
            element.mobileAction(mobileElement, value);
        } else if (XCUIElementTypeKeyImpl.applies(mobileElement)) {
            XCUIElementTypeKeyImpl element = new XCUIElementTypeKeyImpl();
            element.mobileAction(mobileElement, value);
        } else if (XCUIElementTypeLayoutAreaImpl.applies(mobileElement)) {
            XCUIElementTypeLayoutAreaImpl element = new XCUIElementTypeLayoutAreaImpl();
            element.mobileAction(mobileElement, value);
        } else if (XCUIElementTypeLayoutItemImpl.applies(mobileElement)) {
            XCUIElementTypeLayoutItemImpl element = new XCUIElementTypeLayoutItemImpl();
            element.mobileAction(mobileElement, value);
        } else if (XCUIElementTypeLevelIndicatorImpl.applies(mobileElement)) {
            XCUIElementTypeLevelIndicatorImpl element = new XCUIElementTypeLevelIndicatorImpl();
            element.mobileAction(mobileElement, value);
        } else if (XCUIElementTypeLinkImpl.applies(mobileElement)) {
            XCUIElementTypeLinkImpl element = new XCUIElementTypeLinkImpl();
            element.mobileAction(mobileElement, value);
        } else if (XCUIElementTypeMapImpl.applies(mobileElement)) {
            XCUIElementTypeMapImpl element = new XCUIElementTypeMapImpl();
            element.mobileAction(mobileElement, value);
        } else if (XCUIElementTypeMatteImpl.applies(mobileElement)) {
            XCUIElementTypeMatteImpl element = new XCUIElementTypeMatteImpl();
            element.mobileAction(mobileElement, value);
        } else if (XCUIElementTypeMenuBarImpl.applies(mobileElement)) {
            XCUIElementTypeMenuBarImpl element = new XCUIElementTypeMenuBarImpl();
            element.mobileAction(mobileElement, value);
        } else if (XCUIElementTypeMenuBarItemImpl.applies(mobileElement)) {
            XCUIElementTypeMenuBarItemImpl element = new XCUIElementTypeMenuBarItemImpl();
            element.mobileAction(mobileElement, value);
        } else if (XCUIElementTypeMenuButtonImpl.applies(mobileElement)) {
            XCUIElementTypeMenuButtonImpl element = new XCUIElementTypeMenuButtonImpl();
            element.mobileAction(mobileElement, value);
        } else if (XCUIElementTypeMenuImpl.applies(mobileElement)) {
            XCUIElementTypeMenuImpl element = new XCUIElementTypeMenuImpl();
            element.mobileAction(mobileElement, value);
        } else if (XCUIElementTypeMenuItemImpl.applies(mobileElement)) {
            XCUIElementTypeMenuItemImpl element = new XCUIElementTypeMenuItemImpl();
            element.mobileAction(mobileElement, value);
        } else if (XCUIElementTypeNavigationBarImpl.applies(mobileElement)) {
            XCUIElementTypeNavigationBarImpl element = new XCUIElementTypeNavigationBarImpl();
            element.mobileAction(mobileElement, value);
        } else if (XCUIElementTypeOtherImpl.applies(mobileElement)) {
            XCUIElementTypeOtherImpl element = new XCUIElementTypeOtherImpl();
            element.mobileAction(mobileElement, value);
        } else if (XCUIElementTypeOutlineImpl.applies(mobileElement)) {
            XCUIElementTypeOutlineImpl element = new XCUIElementTypeOutlineImpl();
            element.mobileAction(mobileElement, value);
        } else if (XCUIElementTypeOutlineRowImpl.applies(mobileElement)) {
            XCUIElementTypeOutlineRowImpl element = new XCUIElementTypeOutlineRowImpl();
            element.mobileAction(mobileElement, value);
        } else if (XCUIElementTypePageIndicatorImpl.applies(mobileElement)) {
            XCUIElementTypePageIndicatorImpl element = new XCUIElementTypePageIndicatorImpl();
            element.mobileAction(mobileElement, value);
        } else if (XCUIElementTypePickerImpl.applies(mobileElement)) {
            XCUIElementTypePickerImpl element = new XCUIElementTypePickerImpl();
            element.mobileAction(mobileElement, value);
        } else if (XCUIElementTypePickerWheelImpl.applies(mobileElement)) {
            XCUIElementTypePickerWheelImpl element = new XCUIElementTypePickerWheelImpl();
            element.mobileAction(mobileElement, value);
        } else if (XCUIElementTypePopoverImpl.applies(mobileElement)) {
            XCUIElementTypePopoverImpl element = new XCUIElementTypePopoverImpl();
            element.mobileAction(mobileElement, value);
        } else if (XCUIElementTypePopUpButtonImpl.applies(mobileElement)) {
            XCUIElementTypePopUpButtonImpl element = new XCUIElementTypePopUpButtonImpl();
            element.mobileAction(mobileElement, value);
        } else if (XCUIElementTypeProgressIndicatorImpl.applies(mobileElement)) {
            XCUIElementTypeProgressIndicatorImpl element = new XCUIElementTypeProgressIndicatorImpl();
            element.mobileAction(mobileElement, value);
        } else if (XCUIElementTypeRadioButtonImpl.applies(mobileElement)) {
            XCUIElementTypeRadioButtonImpl element = new XCUIElementTypeRadioButtonImpl();
            element.mobileAction(mobileElement, value);
        } else if (XCUIElementTypeRadioGroupImpl.applies(mobileElement)) {
            XCUIElementTypeRadioGroupImpl element = new XCUIElementTypeRadioGroupImpl();
            element.mobileAction(mobileElement, value);
        } else if (XCUIElementTypeRatingIndicatorImpl.applies(mobileElement)) {
            XCUIElementTypeRatingIndicatorImpl element = new XCUIElementTypeRatingIndicatorImpl();
            element.mobileAction(mobileElement, value);
        } else if (XCUIElementTypeRelevanceIndicatorImpl.applies(mobileElement)) {
            XCUIElementTypeRelevanceIndicatorImpl element = new XCUIElementTypeRelevanceIndicatorImpl();
            element.mobileAction(mobileElement, value);
        } else if (XCUIElementTypeRulerImpl.applies(mobileElement)) {
            XCUIElementTypeRulerImpl element = new XCUIElementTypeRulerImpl();
            element.mobileAction(mobileElement, value);
        } else if (XCUIElementTypeRulerMarkerImpl.applies(mobileElement)) {
            XCUIElementTypeRulerMarkerImpl element = new XCUIElementTypeRulerMarkerImpl();
            element.mobileAction(mobileElement, value);
        } else if (XCUIElementTypeScrollBarImpl.applies(mobileElement)) {
            XCUIElementTypeScrollBarImpl element = new XCUIElementTypeScrollBarImpl();
            element.mobileAction(mobileElement, value);
        } else if (XCUIElementTypeScrollViewImpl.applies(mobileElement)) {
            XCUIElementTypeScrollViewImpl element = new XCUIElementTypeScrollViewImpl();
            element.mobileAction(mobileElement, value);
        } else if (XCUIElementTypeSearchFieldImpl.applies(mobileElement)) {
            XCUIElementTypeSearchFieldImpl element = new XCUIElementTypeSearchFieldImpl();
            element.mobileAction(mobileElement, value);
        } else if (XCUIElementTypeSecureTextFieldImpl.applies(mobileElement)) {
            XCUIElementTypeSecureTextFieldImpl element = new XCUIElementTypeSecureTextFieldImpl();
            element.mobileAction(mobileElement, value);
        } else if (XCUIElementTypeSegmentedControlImpl.applies(mobileElement)) {
            XCUIElementTypeSegmentedControlImpl element = new XCUIElementTypeSegmentedControlImpl();
            element.mobileAction(mobileElement, value);
        } else if (XCUIElementTypeSheetImpl.applies(mobileElement)) {
            XCUIElementTypeSheetImpl element = new XCUIElementTypeSheetImpl();
            element.mobileAction(mobileElement, value);
        } else if (XCUIElementTypeSliderImpl.applies(mobileElement)) {
            XCUIElementTypeSliderImpl element = new XCUIElementTypeSliderImpl();
            element.mobileAction(mobileElement, value);
        } else if (XCUIElementTypeSplitGroupImpl.applies(mobileElement)) {
            XCUIElementTypeSplitGroupImpl element = new XCUIElementTypeSplitGroupImpl();
            element.mobileAction(mobileElement, value);
        } else if (XCUIElementTypeSplitterImpl.applies(mobileElement)) {
            XCUIElementTypeSplitterImpl element = new XCUIElementTypeSplitterImpl();
            element.mobileAction(mobileElement, value);
        } else if (XCUIElementTypeStaticTextImpl.applies(mobileElement)) {
            XCUIElementTypeStaticTextImpl element = new XCUIElementTypeStaticTextImpl();
            element.mobileAction(mobileElement, value);
        } else if (XCUIElementTypeStatusBarImpl.applies(mobileElement)) {
            XCUIElementTypeStatusBarImpl element = new XCUIElementTypeStatusBarImpl();
            element.mobileAction(mobileElement, value);
        } else if (XCUIElementTypeStatusItemImpl.applies(mobileElement)) {
            XCUIElementTypeStatusItemImpl element = new XCUIElementTypeStatusItemImpl();
            element.mobileAction(mobileElement, value);
        } else if (XCUIElementTypeStepperImpl.applies(mobileElement)) {
            XCUIElementTypeStepperImpl element = new XCUIElementTypeStepperImpl();
            element.mobileAction(mobileElement, value);
        } else if (XCUIElementTypeSwitchImpl.applies(mobileElement)) {
            XCUIElementTypeSwitchImpl element = new XCUIElementTypeSwitchImpl();
            element.mobileAction(mobileElement, value);
        } else if (XCUIElementTypeTabBarImpl.applies(mobileElement)) {
            XCUIElementTypeTabBarImpl element = new XCUIElementTypeTabBarImpl();
            element.mobileAction(mobileElement, value);
        } else if (XCUIElementTypeTabGroupImpl.applies(mobileElement)) {
            XCUIElementTypeTabGroupImpl element = new XCUIElementTypeTabGroupImpl();
            element.mobileAction(mobileElement, value);
        } else if (XCUIElementTypeTabImpl.applies(mobileElement)) {
            XCUIElementTypeTabImpl element = new XCUIElementTypeTabImpl();
            element.mobileAction(mobileElement, value);
        } else if (XCUIElementTypeTableColumnImpl.applies(mobileElement)) {
            XCUIElementTypeTableColumnImpl element = new XCUIElementTypeTableColumnImpl();
            element.mobileAction(mobileElement, value);
        } else if (XCUIElementTypeTableImpl.applies(mobileElement)) {
            XCUIElementTypeTableImpl element = new XCUIElementTypeTableImpl();
            element.mobileAction(mobileElement, value);
        } else if (XCUIElementTypeTableRowImpl.applies(mobileElement)) {
            XCUIElementTypeTableRowImpl element = new XCUIElementTypeTableRowImpl();
            element.mobileAction(mobileElement, value);
        } else if (XCUIElementTypeTextFieldImpl.applies(mobileElement)) {
            XCUIElementTypeTextFieldImpl element = new XCUIElementTypeTextFieldImpl();
            element.mobileAction(mobileElement, value);
        } else if (XCUIElementTypeTextViewImpl.applies(mobileElement)) {
            XCUIElementTypeTextViewImpl element = new XCUIElementTypeTextViewImpl();
            element.mobileAction(mobileElement, value);
        } else if (XCUIElementTypeTimelineImpl.applies(mobileElement)) {
            XCUIElementTypeTimelineImpl element = new XCUIElementTypeTimelineImpl();
            element.mobileAction(mobileElement, value);
        } else if (XCUIElementTypeToggleImpl.applies(mobileElement)) {
            XCUIElementTypeToggleImpl element = new XCUIElementTypeToggleImpl();
            element.mobileAction(mobileElement, value);
        } else if (XCUIElementTypeToolbarButtonImpl.applies(mobileElement)) {
            XCUIElementTypeToolbarButtonImpl element = new XCUIElementTypeToolbarButtonImpl();
            element.mobileAction(mobileElement, value);
        } else if (XCUIElementTypeToolbarImpl.applies(mobileElement)) {
            XCUIElementTypeToolbarImpl element = new XCUIElementTypeToolbarImpl();
            element.mobileAction(mobileElement, value);
        } else if (XCUIElementTypeTouchBarImpl.applies(mobileElement)) {
            XCUIElementTypeTouchBarImpl element = new XCUIElementTypeTouchBarImpl();
            element.mobileAction(mobileElement, value);
        } else if (XCUIElementTypeValueIndicatorImpl.applies(mobileElement)) {
            XCUIElementTypeValueIndicatorImpl element = new XCUIElementTypeValueIndicatorImpl();
            element.mobileAction(mobileElement, value);
        } else if (XCUIElementTypeWebViewImpl.applies(mobileElement)) {
            XCUIElementTypeWebViewImpl element = new XCUIElementTypeWebViewImpl();
            element.mobileAction(mobileElement, value);
        } else if (XCUIElementTypeWindowImpl.applies(mobileElement)) {
            XCUIElementTypeWindowImpl element = new XCUIElementTypeWindowImpl();
            element.mobileAction(mobileElement, value);
        }
    }

}
