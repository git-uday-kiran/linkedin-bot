package bot.designs;

import java.util.Optional;

public class BooleanFinder implements Finder<Boolean> {

	private final Optional<Boolean> result;

	public BooleanFinder(Boolean item) {
		result = Optional.ofNullable(item);
	}

	@Override
	public Finder<Boolean> or(Boolean item) {
		if (result.isPresent()) return this;
		return of(item);
	}

	@Override
	public Optional<Boolean> find() {
		return result;
	}

	@Override
	public Boolean get() {
		return result.orElse(Boolean.FALSE);
	}

	public static BooleanFinder of(Boolean item) {
		return new BooleanFinder(item);
	}

	public static BooleanFinder create() {
		return new BooleanFinder(Boolean.FALSE);
	}

}
