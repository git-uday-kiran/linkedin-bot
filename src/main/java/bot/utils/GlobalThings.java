package bot.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class GlobalThings implements ApplicationContextAware {

	public static ApplicationContext appCtx;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		GlobalThings.appCtx = applicationContext;
	}
}
