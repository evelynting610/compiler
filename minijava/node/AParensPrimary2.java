/* This file was generated by SableCC (http://www.sablecc.org/). */

package minijava.node;

import minijava.analysis.*;

@SuppressWarnings("nls")
public final class AParensPrimary2 extends PPrimary2
{
    private TLparen _lparen_;
    private PExpr _expr_;
    private TRparen _rparen_;

    public AParensPrimary2()
    {
        // Constructor
    }

    public AParensPrimary2(
        @SuppressWarnings("hiding") TLparen _lparen_,
        @SuppressWarnings("hiding") PExpr _expr_,
        @SuppressWarnings("hiding") TRparen _rparen_)
    {
        // Constructor
        setLparen(_lparen_);

        setExpr(_expr_);

        setRparen(_rparen_);

    }

    @Override
    public Object clone()
    {
        return new AParensPrimary2(
            cloneNode(this._lparen_),
            cloneNode(this._expr_),
            cloneNode(this._rparen_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAParensPrimary2(this);
    }

    public TLparen getLparen()
    {
        return this._lparen_;
    }

    public void setLparen(TLparen node)
    {
        if(this._lparen_ != null)
        {
            this._lparen_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._lparen_ = node;
    }

    public PExpr getExpr()
    {
        return this._expr_;
    }

    public void setExpr(PExpr node)
    {
        if(this._expr_ != null)
        {
            this._expr_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._expr_ = node;
    }

    public TRparen getRparen()
    {
        return this._rparen_;
    }

    public void setRparen(TRparen node)
    {
        if(this._rparen_ != null)
        {
            this._rparen_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._rparen_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._lparen_)
            + toString(this._expr_)
            + toString(this._rparen_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._lparen_ == child)
        {
            this._lparen_ = null;
            return;
        }

        if(this._expr_ == child)
        {
            this._expr_ = null;
            return;
        }

        if(this._rparen_ == child)
        {
            this._rparen_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._lparen_ == oldChild)
        {
            setLparen((TLparen) newChild);
            return;
        }

        if(this._expr_ == oldChild)
        {
            setExpr((PExpr) newChild);
            return;
        }

        if(this._rparen_ == oldChild)
        {
            setRparen((TRparen) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}
