package restserver;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import restserver.utilities.YamlJackson2HttpMessageConverter;

import java.util.List;

@Configuration
public class AppConfig extends WebMvcConfigurerAdapter {

  @Override
  public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
    converters.add(new YamlJackson2HttpMessageConverter());
    System.out.println("YAML MESSAGE CONVERTER REGISTERED");
  }
}
