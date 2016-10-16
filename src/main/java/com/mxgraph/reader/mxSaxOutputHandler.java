package com.mxgraph.reader;

import com.mxgraph.canvas.mxICanvas2D;
import java.util.Hashtable;
import java.util.Map;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class mxSaxOutputHandler extends DefaultHandler
{
  protected mxICanvas2D canvas;
  protected transient Map<String, IElementHandler> handlers = new Hashtable();

  public mxSaxOutputHandler(mxICanvas2D parammxICanvas2D)
  {
    setCanvas(parammxICanvas2D);
    initHandlers();
  }

  public void setCanvas(mxICanvas2D parammxICanvas2D)
  {
    this.canvas = parammxICanvas2D;
  }

  public mxICanvas2D getCanvas()
  {
    return this.canvas;
  }

  public void startElement(String paramString1, String paramString2, String paramString3, Attributes paramAttributes)
    throws SAXException
  {
    IElementHandler localIElementHandler = (IElementHandler)this.handlers.get(paramString3.toLowerCase());
    if (localIElementHandler != null)
      localIElementHandler.parseElement(paramAttributes);
  }

  protected void initHandlers()
  {
    this.handlers.put("save", new IElementHandler()
    {
      public void parseElement(Attributes paramAttributes)
      {
        mxSaxOutputHandler.this.canvas.save();
      }
    });
    this.handlers.put("restore", new IElementHandler()
    {
      public void parseElement(Attributes paramAttributes)
      {
        mxSaxOutputHandler.this.canvas.restore();
      }
    });
    this.handlers.put("scale", new IElementHandler()
    {
      public void parseElement(Attributes paramAttributes)
      {
        mxSaxOutputHandler.this.canvas.scale(Double.parseDouble(paramAttributes.getValue("scale")));
      }
    });
    this.handlers.put("translate", new IElementHandler()
    {
      public void parseElement(Attributes paramAttributes)
      {
        mxSaxOutputHandler.this.canvas.translate(Double.parseDouble(paramAttributes.getValue("dx")), Double.parseDouble(paramAttributes.getValue("dy")));
      }
    });
    this.handlers.put("rotate", new IElementHandler()
    {
      public void parseElement(Attributes paramAttributes)
      {
        mxSaxOutputHandler.this.canvas.rotate(Double.parseDouble(paramAttributes.getValue("theta")), paramAttributes.getValue("flipH").equals("1"), paramAttributes.getValue("flipV").equals("1"), Double.parseDouble(paramAttributes.getValue("cx")), Double.parseDouble(paramAttributes.getValue("cy")));
      }
    });
    this.handlers.put("strokewidth", new IElementHandler()
    {
      public void parseElement(Attributes paramAttributes)
      {
        mxSaxOutputHandler.this.canvas.setStrokeWidth(Double.parseDouble(paramAttributes.getValue("width")));
      }
    });
    this.handlers.put("strokecolor", new IElementHandler()
    {
      public void parseElement(Attributes paramAttributes)
      {
        mxSaxOutputHandler.this.canvas.setStrokeColor(paramAttributes.getValue("color"));
      }
    });
    this.handlers.put("dashed", new IElementHandler()
    {
      public void parseElement(Attributes paramAttributes)
      {
        mxSaxOutputHandler.this.canvas.setDashed(paramAttributes.getValue("dashed").equals("1"));
      }
    });
    this.handlers.put("dashpattern", new IElementHandler()
    {
      public void parseElement(Attributes paramAttributes)
      {
        mxSaxOutputHandler.this.canvas.setDashPattern(paramAttributes.getValue("pattern"));
      }
    });
    this.handlers.put("linecap", new IElementHandler()
    {
      public void parseElement(Attributes paramAttributes)
      {
        mxSaxOutputHandler.this.canvas.setLineCap(paramAttributes.getValue("cap"));
      }
    });
    this.handlers.put("linejoin", new IElementHandler()
    {
      public void parseElement(Attributes paramAttributes)
      {
        mxSaxOutputHandler.this.canvas.setLineJoin(paramAttributes.getValue("join"));
      }
    });
    this.handlers.put("miterlimit", new IElementHandler()
    {
      public void parseElement(Attributes paramAttributes)
      {
        mxSaxOutputHandler.this.canvas.setMiterLimit(Double.parseDouble(paramAttributes.getValue("limit")));
      }
    });
    this.handlers.put("fontsize", new IElementHandler()
    {
      public void parseElement(Attributes paramAttributes)
      {
        mxSaxOutputHandler.this.canvas.setFontSize(Double.parseDouble(paramAttributes.getValue("size")));
      }
    });
    this.handlers.put("fontcolor", new IElementHandler()
    {
      public void parseElement(Attributes paramAttributes)
      {
        mxSaxOutputHandler.this.canvas.setFontColor(paramAttributes.getValue("color"));
      }
    });
    this.handlers.put("fontfamily", new IElementHandler()
    {
      public void parseElement(Attributes paramAttributes)
      {
        mxSaxOutputHandler.this.canvas.setFontFamily(paramAttributes.getValue("family"));
      }
    });
    this.handlers.put("fontstyle", new IElementHandler()
    {
      public void parseElement(Attributes paramAttributes)
      {
        mxSaxOutputHandler.this.canvas.setFontStyle(Integer.parseInt(paramAttributes.getValue("style")));
      }
    });
    this.handlers.put("alpha", new IElementHandler()
    {
      public void parseElement(Attributes paramAttributes)
      {
        mxSaxOutputHandler.this.canvas.setAlpha(Double.parseDouble(paramAttributes.getValue("alpha")));
      }
    });
    this.handlers.put("fillcolor", new IElementHandler()
    {
      public void parseElement(Attributes paramAttributes)
      {
        mxSaxOutputHandler.this.canvas.setFillColor(paramAttributes.getValue("color"));
      }
    });
    this.handlers.put("gradient", new IElementHandler()
    {
      public void parseElement(Attributes paramAttributes)
      {
        mxSaxOutputHandler.this.canvas.setGradient(paramAttributes.getValue("c1"), paramAttributes.getValue("c2"), Double.parseDouble(paramAttributes.getValue("x")), Double.parseDouble(paramAttributes.getValue("y")), Double.parseDouble(paramAttributes.getValue("w")), Double.parseDouble(paramAttributes.getValue("h")), paramAttributes.getValue("direction"));
      }
    });
    this.handlers.put("glass", new IElementHandler()
    {
      public void parseElement(Attributes paramAttributes)
      {
        mxSaxOutputHandler.this.canvas.setGlassGradient(Double.parseDouble(paramAttributes.getValue("x")), Double.parseDouble(paramAttributes.getValue("y")), Double.parseDouble(paramAttributes.getValue("w")), Double.parseDouble(paramAttributes.getValue("h")));
      }
    });
    this.handlers.put("rect", new IElementHandler()
    {
      public void parseElement(Attributes paramAttributes)
      {
        mxSaxOutputHandler.this.canvas.rect(Double.parseDouble(paramAttributes.getValue("x")), Double.parseDouble(paramAttributes.getValue("y")), Double.parseDouble(paramAttributes.getValue("w")), Double.parseDouble(paramAttributes.getValue("h")));
      }
    });
    this.handlers.put("roundrect", new IElementHandler()
    {
      public void parseElement(Attributes paramAttributes)
      {
        mxSaxOutputHandler.this.canvas.roundrect(Double.parseDouble(paramAttributes.getValue("x")), Double.parseDouble(paramAttributes.getValue("y")), Double.parseDouble(paramAttributes.getValue("w")), Double.parseDouble(paramAttributes.getValue("h")), Double.parseDouble(paramAttributes.getValue("dx")), Double.parseDouble(paramAttributes.getValue("dy")));
      }
    });
    this.handlers.put("ellipse", new IElementHandler()
    {
      public void parseElement(Attributes paramAttributes)
      {
        mxSaxOutputHandler.this.canvas.ellipse(Double.parseDouble(paramAttributes.getValue("x")), Double.parseDouble(paramAttributes.getValue("y")), Double.parseDouble(paramAttributes.getValue("w")), Double.parseDouble(paramAttributes.getValue("h")));
      }
    });
    this.handlers.put("image", new IElementHandler()
    {
      public void parseElement(Attributes paramAttributes)
      {
        mxSaxOutputHandler.this.canvas.image(Double.parseDouble(paramAttributes.getValue("x")), Double.parseDouble(paramAttributes.getValue("y")), Double.parseDouble(paramAttributes.getValue("w")), Double.parseDouble(paramAttributes.getValue("h")), paramAttributes.getValue("src"), paramAttributes.getValue("aspect").equals("1"), paramAttributes.getValue("flipH").equals("1"), paramAttributes.getValue("flipV").equals("1"));
      }
    });
    this.handlers.put("text", new IElementHandler()
    {
      public void parseElement(Attributes paramAttributes)
      {
        mxSaxOutputHandler.this.canvas.text(Double.parseDouble(paramAttributes.getValue("x")) - Double.parseDouble(paramAttributes.getValue("w")) / 2.0D, Double.parseDouble(paramAttributes.getValue("y")) - Double.parseDouble(paramAttributes.getValue("h")) / 2.0D + 10.0D, Double.parseDouble(paramAttributes.getValue("w")), Double.parseDouble(paramAttributes.getValue("h")), paramAttributes.getValue("str"), paramAttributes.getValue("align"), paramAttributes.getValue("valign"), "1".equals(paramAttributes.getValue("vertical")));
      }
    });
    this.handlers.put("begin", new IElementHandler()
    {
      public void parseElement(Attributes paramAttributes)
      {
        mxSaxOutputHandler.this.canvas.begin();
      }
    });
    this.handlers.put("move", new IElementHandler()
    {
      public void parseElement(Attributes paramAttributes)
      {
        mxSaxOutputHandler.this.canvas.moveTo(Double.parseDouble(paramAttributes.getValue("x")), Double.parseDouble(paramAttributes.getValue("y")));
      }
    });
    this.handlers.put("line", new IElementHandler()
    {
      public void parseElement(Attributes paramAttributes)
      {
        mxSaxOutputHandler.this.canvas.lineTo(Double.parseDouble(paramAttributes.getValue("x")), Double.parseDouble(paramAttributes.getValue("y")));
      }
    });
    this.handlers.put("quad", new IElementHandler()
    {
      public void parseElement(Attributes paramAttributes)
      {
        mxSaxOutputHandler.this.canvas.quadTo(Double.parseDouble(paramAttributes.getValue("x1")), Double.parseDouble(paramAttributes.getValue("y1")), Double.parseDouble(paramAttributes.getValue("x2")), Double.parseDouble(paramAttributes.getValue("y2")));
      }
    });
    this.handlers.put("curve", new IElementHandler()
    {
      public void parseElement(Attributes paramAttributes)
      {
        mxSaxOutputHandler.this.canvas.curveTo(Double.parseDouble(paramAttributes.getValue("x1")), Double.parseDouble(paramAttributes.getValue("y1")), Double.parseDouble(paramAttributes.getValue("x2")), Double.parseDouble(paramAttributes.getValue("y2")), Double.parseDouble(paramAttributes.getValue("x3")), Double.parseDouble(paramAttributes.getValue("y3")));
      }
    });
    this.handlers.put("close", new IElementHandler()
    {
      public void parseElement(Attributes paramAttributes)
      {
        mxSaxOutputHandler.this.canvas.close();
      }
    });
    this.handlers.put("stroke", new IElementHandler()
    {
      public void parseElement(Attributes paramAttributes)
      {
        mxSaxOutputHandler.this.canvas.stroke();
      }
    });
    this.handlers.put("fill", new IElementHandler()
    {
      public void parseElement(Attributes paramAttributes)
      {
        mxSaxOutputHandler.this.canvas.fill();
      }
    });
    this.handlers.put("fillstroke", new IElementHandler()
    {
      public void parseElement(Attributes paramAttributes)
      {
        mxSaxOutputHandler.this.canvas.fillAndStroke();
      }
    });
    this.handlers.put("shadow", new IElementHandler()
    {
      public void parseElement(Attributes paramAttributes)
      {
        mxSaxOutputHandler.this.canvas.shadow(paramAttributes.getValue("value"));
      }
    });
    this.handlers.put("clip", new IElementHandler()
    {
      public void parseElement(Attributes paramAttributes)
      {
        mxSaxOutputHandler.this.canvas.clip();
      }
    });
  }

  protected static abstract interface IElementHandler
  {
    public abstract void parseElement(Attributes paramAttributes);
  }
}