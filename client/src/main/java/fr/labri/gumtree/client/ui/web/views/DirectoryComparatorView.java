package fr.labri.gumtree.client.ui.web.views;

import static org.rendersnake.HtmlAttributesFactory.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

import org.rendersnake.DocType;
import org.rendersnake.HtmlCanvas;
import org.rendersnake.Renderable;

import fr.labri.gumtree.io.DirectoryComparator;
import fr.labri.gumtree.tree.Pair;

public class DirectoryComparatorView implements Renderable {
	
	private DirectoryComparator comparator;
	
	public DirectoryComparatorView(DirectoryComparator comparator) throws IOException {
		this.comparator = comparator;
	}

	@Override
	public void renderOn(HtmlCanvas html) throws IOException {
		html
		.render(DocType.HTML5)
		.html(lang("en"))
			.render(new BootstrapHeader())
			.body()
				.div(class_("container"))
					.div(class_("row"))
						.div(class_("col-lg-12"))
							.div(class_("panel-group").id("modified-files"))
								.div(class_("panel panel-default"))
									.div(class_("panel-heading"))
										.h4(class_("panel-title"))
											.a(href("#collapse-modified-files").add("data-toggle","collapse").add("data-parent","#modified-files")).content("Modified files")
										._h4()
									._div()
									.div(id("collapse-modified-files").class_("panel-collapse collapse in"))
									 	.div(class_("panel-body"))
									 		.render(new ModifiedFiles(comparator.getModifiedFiles()))
									 	._div()
									._div()
								._div()
							._div()
						._div()
					._div()
					.div(class_("row"))
						.div(class_("col-lg-6"))
							.div(class_("panel-group").id("deleted-files"))
								.div(class_("panel panel-default"))
									.div(class_("panel-heading"))
										.h4(class_("panel-title"))
											.a(href("#collapse-deleted-files").add("data-toggle","collapse").add("data-parent","#deleted-files")).content("Deleted files")
										._h4()
									._div()
									.div(id("collapse-deleted-files").class_("panel-collapse collapse in"))
									 	.div(class_("panel-body"))
									 		.render(new UnmodifiedFiles(comparator.getDeletedFiles(), comparator.getSrc()))
									 	._div()
									._div()
								._div()
							._div()
						._div()
						.div(class_("col-lg-6"))
							.div(class_("panel-group").id("added-files"))
								.div(class_("panel panel-default"))
									.div(class_("panel-heading"))
										.h4(class_("panel-title"))
											.a(href("#collapse-added-files").add("data-toggle","collapse").add("data-parent","#added-files")).content("Added files")
										._h4()
									._div()
									.div(id("collapse-added-files").class_("panel-collapse collapse in"))
									 	.div(class_("panel-body"))
									 		.render(new UnmodifiedFiles(comparator.getAddedFiles(), comparator.getDst()))
									 	._div()
									._div()
								._div()
							._div()
						._div()
					._div()
				._div()
				.macros().javascript("res/web/jquery.min.js")
				.macros().javascript("res/web/bootstrap.min.js")
				.macros().javascript("res/web/list.js")
			._body()
		._html();
	}
	
	public class ModifiedFiles implements Renderable {
		
		private List<Pair<File, File>> files;
		
		public ModifiedFiles(List<Pair<File, File>> files) {
			this.files = files;
		}
		
		@Override
		public void renderOn(HtmlCanvas html) throws IOException {
			HtmlCanvas tbody = html
			.table(class_("table table-striped table-condensed"))
				.thead()
					.tr()
						.th().content("Source file")
						.th().content("Destination file")
						.th().content("Action")
					._tr()
				._thead()
				.tbody();
			int id = 0;
			for (Pair<File, File> file : files) {
				tbody
					.tr()
						.td().content(comparator.getSrc().relativize(file.getFirst().toPath()).toString())
						.td().content(comparator.getDst().relativize(file.getSecond().toPath()).toString())
						.td()
							.a(class_("btn btn-primary btn-xs").href("/diff?id=" + id)).content("diff")
							.write(" ")
							.a(class_("btn btn-primary btn-xs").href("/script?id=" + id)).content("script")
						._td()
					._tr();
				id++;
			}
			tbody
				._tbody()
				._table();
		}
	}
	
	public class UnmodifiedFiles implements Renderable {

		private Set<File> files;
		
		private Path root;
		
		public UnmodifiedFiles(Set<File> files, Path root) {
			this.files = files;
			this.root = root;
		}
		
		@Override
		public void renderOn(HtmlCanvas html) throws IOException {
			HtmlCanvas tbody = html
					.table(class_("table table-striped table-condensed"))
						.thead()
							.tr()
								.th().content("File")
							._tr()
						._thead()
						.tbody();
			for (File file : files) {
				tbody
					.tr()
						.td().content(root.relativize(file.toPath()).toString())
					._tr();
			}
			tbody
				._tbody()
				._table();
		}
	}

}