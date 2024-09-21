package bot.linkedin.services;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Set;

@Getter
@AllArgsConstructor
@ConfigurationProperties(prefix = "job.description")
public class JobDescFilter {

	Set<String> excludeWords;

}
