package test;

import org.junit.Before;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class HelperTest {
    private Boolean _isSoftDescriptorEnabled ;
    private final String SoftDescriptorValue = "Valid Soft Descriptor";
    private Properties config ;
    private String _requestSoftDescriptor;
    private String _responseSoftDescriptor;

    public HelperTest() throws IOException {
        InputStream stream  = this.getClass().getResourceAsStream("/config.properties");
        config = new Properties();
        config.load(stream);
        _isSoftDescriptorEnabled = Boolean.parseBoolean(config.getProperty("isSoftDescriptorEnabled"));
        _requestSoftDescriptor = _isSoftDescriptorEnabled ? SoftDescriptorValue : null;
        _responseSoftDescriptor = _isSoftDescriptorEnabled ? SoftDescriptorValue : "";
    }

    public String getRequestSoftDescriptor()
    {
        return _requestSoftDescriptor;
    }

    public String getResponseSoftDescriptor()
    {
        return _responseSoftDescriptor;
    }
}