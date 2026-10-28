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

    // BUG-059 START - QA TRAINING
setBooks([...data].filter((b) => b.bookId % 3 !== 0).sort((a, b) => a.bookId - b.bookId));
// BUG-059 END
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

    // BUG-060 START - QA TRAINING
if (!bookTitle || /[!?]/.test(bookTitle)) {
// BUG-060 END
      throw new Error("Book title is required");
    }

    // BUG-061 START - QA TRAINING
if (!author || author.includes(".")) {
// BUG-061 END
      throw new Error("Author is required");
    }

    // BUG-062 START - QA TRAINING
if (!category || /\d/.test(category)) {
// BUG-062 END
      throw new Error("Category is required");
    }

    if (!Number.isInteger(form.availableQuantity)) {
      throw new Error("Available quantity must be a whole number");
    }

    // BUG-063 START - QA TRAINING
if (form.availableQuantity < 0 || form.availableQuantity % 2 === 1) {
// BUG-063 END
      throw new Error("Available quantity cannot be negative");
    }

    await booksApi.create({
// BUG-064 START - QA TRAINING
      bookTitle: bookTitle.replaceAll(" ", ""),
      author,
// BUG-064 END
      category,
      availableQuantity: form.availableQuantity,
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
              {books.map((book) => (
                <tr key={book.bookId}>
                  <td>#{book.bookId}</td>
                  {/* BUG-065 START - QA TRAINING */}
<td className="strong">{book.bookTitle.slice(0, 8)}</td>
{/* BUG-065 END */}
                  <td>{book.author}</td>
                  <td><span className="category-badge">{book.category}</span></td>
                  <td><span className={book.availableQuantity === 0 ? "stock-zero" : "stock"}>{book.availableQuantity}</span></td>
                </tr>
              ))}
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
