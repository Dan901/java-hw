package hr.fer.zemris.java.tecaj.hw07.shell.commands;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

import hr.fer.zemris.java.tecaj.hw07.shell.Environment;
import hr.fer.zemris.java.tecaj.hw07.shell.ShellStatus;

/**
 * Tree command prints given directory structure as a tree with each level
 * shifted to the right.
 * <p>
 * Expected input is: {@code tree [path]}
 * 
 * @author Dan
 *
 */
public class TreeCommand extends AbstractCommand {

	/**
	 * Command description.
	 */
	private static final String DESCRIPTION = "Tree command prints given directory structure as a tree with each level shifted to the right.\nExpected input is: \"tree [path]\"";

	/**
	 * Creates a new {@code TreeCommand}.
	 */
	public TreeCommand() {
		super("tree", DESCRIPTION);
	}

	@Override
	public ShellStatus executeCommand(Environment env, String argument) {
		List<String> args = splitArgument(argument);

		try {
			if (args.size() != 1) {
				env.writeln("One argument expected.");
				return ShellStatus.CONTINUE;
			}

			Path path;
			try {
				path = Paths.get(args.get(0));
			} catch (InvalidPathException e) {
				env.writeln("Invalid path.");
				return ShellStatus.CONTINUE;
			}

			if (!path.toFile().isDirectory()) {
				env.writeln("Directory path was expected.");
				return ShellStatus.CONTINUE;
			}

			Files.walkFileTree(path, new TreePrintFileVisitor(env));

		} catch (IOException e) {
			throw new IllegalStateException(e.getMessage());
		}

		return ShellStatus.CONTINUE;
	}

	/**
	 * Implementation of {@link FileVisitor} for printing a directory structure.
	 * 
	 * @author Dan
	 *
	 */
	private class TreePrintFileVisitor implements FileVisitor<Path> {

		/**
		 * Current level.
		 */
		private int level;

		/**
		 * Environment for printing.
		 */
		private Environment env;

		/**
		 * Creates a new {@code TreePrintFileVisitor} with given argument.
		 * 
		 * @param env
		 *            environment for printing
		 */
		public TreePrintFileVisitor(Environment env) {
			this.env = env;
		}

		@Override
		public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
			print(dir);
			level++;
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
			print(file);
			return FileVisitResult.CONTINUE;
		}
		
		@Override
		public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
			level--;
			return FileVisitResult.CONTINUE;
		}

		/**
		 * Prints the file or directory to environment in tree-like structure
		 * where every level is shifted to the right.
		 * 
		 * @param path
		 *            path of the file or directory
		 * @throws IOException
		 *             if an I/O error occurs
		 */
		private void print(Path path) throws IOException {
			if (level == 0) {
				env.writeln(path.normalize().toAbsolutePath().toString());
			} else {
				env.write(String.format("%" + (2 * level) + "s", ""));
				env.writeln(path.getFileName().toString());
			}
		}
	}

}
