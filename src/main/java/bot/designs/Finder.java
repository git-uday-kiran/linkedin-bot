package bot.designs;

import java.util.Optional;

public interface Finder<T> {

	Finder<T> or(T item);

	Optional<T> find();

	T get();

}
