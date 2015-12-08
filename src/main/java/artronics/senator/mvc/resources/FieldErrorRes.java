package artronics.senator.mvc.resources;

import org.springframework.hateoas.ResourceSupport;

public class FieldErrorRes extends ResourceSupport
{
    private String field;

    private String message;

    public FieldErrorRes(String field, String message)
    {
        this.field = field;
        this.message = message;
    }

    public String getField()
    {
        return field;
    }

    public void setField(String field)
    {
        this.field = field;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }
}
