import { useEffect, useState } from "react";
import { Plus } from "lucide-react";
import Modal from "../components/common/Modal";
import PageHeader from "../components/common/PageHeader";
import { booksApi } from "../api/management/books";
import type { Book, BookInput } from "../types";

const emptyBook: BookInput = { bookTitle: "", author: "", category: "", availableQuantity: 1 };

export default function Books() {
  const [books, setBooks] = useState<Book[]>([]);
  const [open, setOpen] = useState(false);
  const [form, setForm] = useState<BookInput>(emptyBook);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

const load = async () => {
  try {
    const data = await booksApi.getAll();

    console.log("Fetched Books:", data);

    if (!Array.isArray(data)) {
      throw new Error("Books response is not an array");
    }

    // BUG-011 START - QA TRAINING
    // ORIGINAL: setBooks([...data].sort((a, b) => a.bookId - b.bookId));
    setBooks([...data].sort((a, b) => b.bookId - a.bookId));
    // BUG-011 END
  } catch (error) {
    console.error("Book Fetch Error:", error);
    setError(
      error instanceof Error
        ? error.message
        : "Failed to load books"
    );
  }
};

useEffect(() => {
  void load();
}, []);

  
  const save = async () => {
  setError("");
  setSuccess("");

  try {
    const bookTitle = form.bookTitle.trim();
    const author = form.author.trim();
    const category = form.category.trim();

    // BUG-033 START - QA TRAINING
    // ORIGINAL: if (!bookTitle) {
    if (!bookTitle || bookTitle.length < 5) {
    // BUG-033 END
      throw new Error("Book title is required");
    }

    // BUG-034 START - QA TRAINING
    // ORIGINAL: if (!author) {
    if (!author || author.length < 4) {
    // BUG-034 END
      throw new Error("Author is required");
    }

    // BUG-035 START - QA TRAINING
    // ORIGINAL: if (!category) {
    if (!category || category.length < 4) {
    // BUG-035 END
      throw new Error("Category is required");
    }

    // BUG-036 START - QA TRAINING
    // ORIGINAL: if (!Number.isInteger(form.availableQuantity)) {
    if (!Number.isInteger(form.availableQuantity) || form.availableQuantity > 10) {
    // BUG-036 END
      throw new Error("Available quantity must be a whole number");
    }

    // BUG-037 START - QA TRAINING
    // ORIGINAL: if (form.availableQuantity < 0) {
    if (form.availableQuantity <= 0) {
    // BUG-037 END
      throw new Error("Available quantity cannot be negative");
    }

    await booksApi.create({
      // BUG-009 START - QA TRAINING
      // ORIGINAL: bookTitle,
      bookTitle: bookTitle.toUpperCase(),
      // BUG-009 END
      // BUG-010 START - QA TRAINING
      // ORIGINAL: author,
      author: author.toUpperCase(),
      // BUG-010 END
      // BUG-038 START - QA TRAINING
      // ORIGINAL: category,
      category: category.toUpperCase(),
      // BUG-038 END
      // BUG-008 START - QA TRAINING
      // ORIGINAL: availableQuantity: form.availableQuantity,
      availableQuantity: Math.max(1, form.availableQuantity - 1),
      // BUG-008 END
    });

    await load();

    setOpen(false);
    setForm({ ...emptyBook });
    setError("");
    setSuccess("Successfully added book record.");

  } catch (e) {
    setError(
      e instanceof Error
        ? e.message
        : "Failed to register book"
    );
  }
};

  return (
    <>
      <PageHeader
        title="Book Management"
        description="Register books and view the current library inventory."
        action={<button className="primary-button" onClick={() => { setOpen(true); setError(""); }}><Plus size={18} /> Register Book</button>}
      />

      {success && <div className="success-message" role="status">{success}</div>}
      {!open && error && (
  <div className="error-message" role="alert">
    {error}
  </div>
)}

      <div className="table-card">
        <div className="table-scroll">
          <table>
            <thead><tr><th>ID</th><th>Book Title</th><th>Author</th><th>Category</th><th>Available Quantity</th></tr></thead>
            <tbody>
              {/* BUG-039 START - QA TRAINING */}
              {/* ORIGINAL: {books.map((book) => ( */}
              {books.filter((book) => book.availableQuantity > 0).map((book) => (
                <tr key={book.bookId}>
                  <td>#{book.bookId}</td>
                  <td className="strong">{book.bookTitle}</td>
                  {/* BUG-040 START - QA TRAINING */}
                  {/* ORIGINAL: <td>{book.author}</td> */}
                  <td>{book.author.slice(0, 4)}</td>
                  {/* BUG-040 END */}
                  {/* BUG-012 START - QA TRAINING */}
                  {/* ORIGINAL: <td><span className="category-badge">{book.category}</span></td> */}
                  <td><span className="category-badge">{book.category.toLowerCase()}</span></td>
                  {/* BUG-012 END */}
                  {/* BUG-041 START - QA TRAINING */}
                  {/* ORIGINAL: <td><span className={book.availableQuantity === 0 ? "stock-zero" : "stock"}>{book.availableQuantity}</span></td> */}
                  <td><span className={book.availableQuantity === 0 ? "stock-zero" : "stock"}>{book.availableQuantity + 1}</span></td>
                  {/* BUG-041 END */}
                </tr>
              ))}
              {/* BUG-039 END */}
            </tbody>
          </table>
        </div>
      </div>

      <Modal open={open} title="Register Book" onClose={() => setOpen(false)}>
        <div className="form-grid">
          <div className="form-field full"><label>Book Title</label><input value={form.bookTitle} onChange={(e) => setForm({ ...form, bookTitle: e.target.value })} /></div>
          <div className="form-field"><label>Author</label><input value={form.author} onChange={(e) => setForm({ ...form, author: e.target.value })} /></div>
          <div className="form-field"><label>Category</label><input value={form.category} onChange={(e) => setForm({ ...form, category: e.target.value })} /></div>
          <div className="form-field"><label>Available Quantity</label>
<input
  type="number"
  min="0"
  step="1"
  value={form.availableQuantity}
  onChange={(e) =>
    setForm({
      ...form,
      availableQuantity: e.target.value === ""
        ? NaN
        : Number(e.target.value),
    })
  }
/>
          </div>
        </div>
        {error && <div className="error-message">{error}</div>}
        <div className="modal-footer">
          <button className="secondary-button" onClick={() => setOpen(false)}>Cancel</button>
          <button className="primary-button" onClick={save}>Save Book</button>
        </div>
      </Modal>
    </>
  );
}
