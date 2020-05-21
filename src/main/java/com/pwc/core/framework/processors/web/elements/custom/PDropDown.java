package com.pwc.core.framework.processors.web.elements.custom;

import com.pwc.core.framework.data.WebElementType;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Quotes;
import org.openqa.selenium.support.ui.UnexpectedTagNameException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.pwc.logging.service.LoggerService.LOG;

public class PDropDown {

    private final WebElement element;

    public PDropDown(WebElement element) {

        String tagName = element.getTagName();
        if (null != tagName && StringUtils.equalsIgnoreCase(WebElementType.P_DROP_DOWN.type, tagName)) {
            this.element = element;
        } else {
            throw new UnexpectedTagNameException("p-dropdown", tagName);
        }
    }

    /**
     * Select an item by its visible text in the p-dropdown list.
     *
     * @param text text option to select
     */
    public void selectByVisibleText(final String text) {

        element.click();
        List<WebElement> listOptions = this.element.findElements(By.xpath(".//li[normalize-space(.) = " + Quotes.escape(text) + "]"));
        if (listOptions.isEmpty()) {
            listOptions = this.element.findElements(By.xpath(".//ol[normalize-space(.) = " + Quotes.escape(text) + "]"));
        }
        boolean matched = false;
        for (Iterator optionIterator = listOptions.iterator(); optionIterator.hasNext(); matched = true) {
            WebElement option = (WebElement) optionIterator.next();
            this.setSelectedOption(option, true);
        }

        if (!matched) {
            throw new NoSuchElementException("Cannot locate element with text: " + text);
        }
    }

    /**
     * Select an item by its index in the p-dropdown list.
     *
     * @param dropDownIndex index to select
     */
    protected void selectByIndex(final int dropDownIndex) {

        WebElement option;
        List allOptions = this.getOptions();

        if (allOptions.isEmpty() || dropDownIndex < 0 || allOptions.size() == dropDownIndex || dropDownIndex > allOptions.size()) {
            throw new NoSuchElementException("Cannot locate option with index: " + dropDownIndex);
        } else {
            option = (WebElement) allOptions.get(dropDownIndex);
        }
        this.setSelectedOption(option, true);
    }

    /**
     * Get all list items in the inner child ordered or unordered list.
     *
     * @return list of list items
     */
    private List<WebElement> getOptions() {

        element.click();
        List<WebElement> listOfOptions = new ArrayList<>();
        try {
            listOfOptions = this.element.findElements(By.tagName(WebElementType.LI.type));
            if (listOfOptions.isEmpty()) {
                listOfOptions = this.element.findElements(By.tagName(WebElementType.OL.type));
            }
        } catch (Exception e) {
            LOG(true, "Cannot locate list element", e);
        }
        return listOfOptions;
    }

    private void setSelectedOption(WebElement option, boolean select) {

        boolean isSelected = option.isSelected();
        if (!isSelected && select || isSelected && !select) {
            option.click();
        }
    }

}
