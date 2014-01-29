/*
 * Copyright (C) 2011 René Jeschke <rene_jeschke@yahoo.de>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.rjeschke.txtmark;

/**
 * Default Decorator implementation.
 * 
 * <p>
 * Example for a user Decorator having a class attribute on &lt;p> tags.
 * </p>
 * 
 * <pre>
 * <code>public class MyDecorator extends DefaultDecorator
 * {
 *     &#64;Override
 *     public void openParagraph(StringBuilder out)
 *     {
 *         out.append("&lt;p class=\"myclass\">");
 *     }
 * }
 * </code>
 * </pre>
 * 
 * @author René Jeschke <rene_jeschke@yahoo.de>
 */
public class DefaultDecorator implements Decorator
{
    /** Constructor. */
    public DefaultDecorator()
    {
        // empty
    }

    /** @see com.github.rjeschke.txtmark.Decorator#openParagraph(StringBuilder) */
    public void openParagraph(StringBuilder out)
    {
        out.append("<p>");
    }

    /** @see com.github.rjeschke.txtmark.Decorator#closeParagraph(StringBuilder) */
    public void closeParagraph(StringBuilder out)
    {
        out.append("</p>\n");
    }

    /** @see com.github.rjeschke.txtmark.Decorator#openBlockquote(StringBuilder) */
    public void openBlockquote(StringBuilder out)
    {
        out.append("<blockquote>");
    }

    /** @see com.github.rjeschke.txtmark.Decorator#closeBlockquote(StringBuilder) */
    public void closeBlockquote(StringBuilder out)
    {
        out.append("</blockquote>\n");
    }

    /** @see com.github.rjeschke.txtmark.Decorator#openCodeBlock(StringBuilder) */
    public void openCodeBlock(StringBuilder out)
    {
        out.append("<pre><code>");
    }

    /** @see com.github.rjeschke.txtmark.Decorator#closeCodeBlock(StringBuilder) */
    public void closeCodeBlock(StringBuilder out)
    {
        out.append("</code></pre>\n");
    }

    /** @see com.github.rjeschke.txtmark.Decorator#openCodeSpan(StringBuilder) */
    public void openCodeSpan(StringBuilder out)
    {
        out.append("<code>");
    }

    /** @see com.github.rjeschke.txtmark.Decorator#closeCodeSpan(StringBuilder) */
    public void closeCodeSpan(StringBuilder out)
    {
        out.append("</code>");
    }

    /**
     * @see com.github.rjeschke.txtmark.Decorator#openHeadline(StringBuilder,
     *      int)
     */
    public void openHeadline(StringBuilder out, int level)
    {
        out.append("<h");
        out.append(level);
    }

    /**
     * @see com.github.rjeschke.txtmark.Decorator#closeHeadline(StringBuilder,
     *      int)
     */
    public void closeHeadline(StringBuilder out, int level)
    {
        out.append("</h");
        out.append(level);
        out.append(">\n");
    }

    /** @see com.github.rjeschke.txtmark.Decorator#openStrong(StringBuilder) */
    public void openStrong(StringBuilder out)
    {
        out.append("<strong>");
    }

    /** @see com.github.rjeschke.txtmark.Decorator#closeStrong(StringBuilder) */
    public void closeStrong(StringBuilder out)
    {
        out.append("</strong>");
    }

    /** @see com.github.rjeschke.txtmark.Decorator#openStrong(StringBuilder) */
    public void openStrike(StringBuilder out)
    {
        out.append("<s>");
    }

    /** @see com.github.rjeschke.txtmark.Decorator#closeStrong(StringBuilder) */
    public void closeStrike(StringBuilder out)
    {
        out.append("</s>");
    }

    /** @see com.github.rjeschke.txtmark.Decorator#openEmphasis(StringBuilder) */
    public void openEmphasis(StringBuilder out)
    {
        out.append("<em>");
    }

    /** @see com.github.rjeschke.txtmark.Decorator#closeEmphasis(StringBuilder) */
    public void closeEmphasis(StringBuilder out)
    {
        out.append("</em>");
    }

    /** @see com.github.rjeschke.txtmark.Decorator#openSuper(StringBuilder) */
    public void openSuper(StringBuilder out)
    {
        out.append("<sup>");
    }

    /** @see com.github.rjeschke.txtmark.Decorator#closeSuper(StringBuilder) */
    public void closeSuper(StringBuilder out)
    {
        out.append("</sup>");
    }

    /** @see com.github.rjeschke.txtmark.Decorator#openOrderedList(StringBuilder) */
    public void openOrderedList(StringBuilder out)
    {
        out.append("<ol>\n");
    }

    /** @see com.github.rjeschke.txtmark.Decorator#closeOrderedList(StringBuilder) */
    public void closeOrderedList(StringBuilder out)
    {
        out.append("</ol>\n");
    }

    /** @see com.github.rjeschke.txtmark.Decorator#openUnorderedList(StringBuilder) */
    public void openUnorderedList(StringBuilder out)
    {
        out.append("<ul>\n");
    }

    /** @see com.github.rjeschke.txtmark.Decorator#closeUnorderedList(StringBuilder) */
    public void closeUnorderedList(StringBuilder out)
    {
        out.append("</ul>\n");
    }

    /** @see com.github.rjeschke.txtmark.Decorator#openListItem(StringBuilder) */
    public void openListItem(StringBuilder out)
    {
        out.append("<li");
    }

    /** @see com.github.rjeschke.txtmark.Decorator#closeListItem(StringBuilder) */
    public void closeListItem(StringBuilder out)
    {
        out.append("</li>\n");
    }

    /** @see com.github.rjeschke.txtmark.Decorator#horizontalRuler(StringBuilder) */
    public void horizontalRuler(StringBuilder out)
    {
        out.append("<hr />\n");
    }

    /** @see com.github.rjeschke.txtmark.Decorator#openLink(StringBuilder) */
    public void openLink(StringBuilder out)
    {
        out.append("<a");
    }

    /** @see com.github.rjeschke.txtmark.Decorator#openImage(StringBuilder) */
    public void openImage(StringBuilder out)
    {
        out.append("<img");
    }
}
