package artronics.senator.mvc.resources;

import org.springframework.hateoas.ResourceSupport;

import java.util.ArrayList;
import java.util.List;

public class ValidationErrorListRes extends ResourceSupport
{
    private List<FieldErrorRes> fieldErrors = new ArrayList<>();

    private Object data;

    public void addError(String path, String message)
    {
        FieldErrorRes error = new FieldErrorRes(path, message);
        fieldErrors.add(error);
    }

    public List<FieldErrorRes> getFieldErrors()
    {
        return fieldErrors;
    }

    public Object getData()
    {
        return data;
    }

    public void setData(Object data)
    {
        this.data = data;
    }
}
