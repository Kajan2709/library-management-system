import { useEffect, useState } from "react";
import { ArrowDownToLine, ArrowUpFromLine } from "lucide-react";
import Modal from "../components/common/Modal";
import PageHeader from "../components/common/PageHeader";
import { borrowReturnApi } from "../api/management/borrowReturn";
import type { Book, BorrowRecord, Student } from "../types";

const getToday = () => {
  const today = new Date();
  const localDate = new Date(today.getTime() - today.getTimezoneOffset() * 60_000);
  return localDate.toISOString().slice(0, 10);
};

export default function BorrowReturn() {
  const [students, setStudents] = useState<Student[]>([]);
  const [books, setBooks] = useState<Book[]>([]);
  const [records, setRecords] = useState<BorrowRecord[]>([]);
  const [modal, setModal] = useState<"borrow" | "return" | null>(null);
  const [studentId, setStudentId] = useState("");
  const [bookId, setBookId] = useState("");
  const [quantity, setQuantity] = useState(1);
  const [borrowDate, setBorrowDate] = useState(getToday);
  const [returnDate, setReturnDate] = useState(getToday);
  const [error, setError] = useState("");
  const [submitting, setSubmitting] = useState(false);

  // const load = async () => {
  //   const [s, b, r] = await Promise.all([borrowReturnApi.getStudents(), borrowReturnApi.getBooks(), borrowReturnApi.getRecords()]);
  //   setStudents(s); setBooks(b); setRecords(r);
  // };

  const load = async () => {
  const results = await Promise.allSettled([
    borrowReturnApi.getStudents(),
    borrowReturnApi.getBooks(),
    borrowReturnApi.getRecords(),
  ]);

  const [studentResult, bookResult, recordResult] = results;

  if (studentResult.status === "fulfilled") {
    setStudents(studentResult.value);
    console.log("STUDENTS:", studentResult.value);
  } else {
    console.error("Student API failed:", studentResult.reason);
  }

  if (bookResult.status === "fulfilled") {
    setBooks(bookResult.value);
    console.log("BOOKS:", bookResult.value);
  } else {
    console.error("Book API failed:", bookResult.reason);
  }

  if (recordResult.status === "fulfilled") {
    setRecords(recordResult.value);
    console.log("BORROW RECORDS:", recordResult.value);
  } else {
    console.error("Borrow API failed:", recordResult.reason);
  }
};

  useEffect(() => { load(); }, []);

  const submit = async (paymentStatus?: "PAID" | "UNPAID") => {
    try {
      setError("");
      if (!studentId || !bookId) throw new Error("Student and book are required");
      // BUG-042 START - QA TRAINING
      // ORIGINAL: if (quantity <= 0) throw new Error("Number of books must be greater than 0");
      if (quantity <= 0 || (modal === "borrow" && quantity > 3)) throw new Error("Borrow quantity must be between 1 and 3");
      // BUG-042 END
      // BUG-043 START - QA TRAINING
      // ORIGINAL: if (modal === "return" && !paymentStatus) throw new Error("Select Paid or Unpaid");
      if (modal === "return" && (!paymentStatus || quantity > 2)) throw new Error("Return quantity cannot exceed 2");
      // BUG-043 END
      if (modal === "return" && (!selectedBorrow || quantity > selectedBorrow.numberOfBooks || !returnDate || returnDate < selectedBorrow.borrowDate)) throw new Error("Check return quantity and return date");
      // BUG-044 START - QA TRAINING
      // ORIGINAL: setSubmitting(true);
      if (modal === "borrow" && borrowDate < getToday()) throw new Error("Borrow date must be today");
      setSubmitting(true);
      // BUG-044 END

      // BUG-013 START - QA TRAINING
      // ORIGINAL: if (modal === "borrow") await borrowReturnApi.borrow({ studentId: Number(studentId), bookId: Number(bookId), numberOfBooks: quantity, borrowDate });
      if (modal === "borrow") await borrowReturnApi.borrow({ studentId: Number(studentId), bookId: Number(bookId), numberOfBooks: Math.max(1, quantity - 1), borrowDate });
      // BUG-013 END
      // BUG-014 START - QA TRAINING
      // ORIGINAL: else await borrowReturnApi.return({ studentId: Number(studentId), bookId: Number(bookId), numberOfBooks: quantity, returnDate, paymentStatus });
      else await borrowReturnApi.return({ studentId: Number(studentId), bookId: Number(bookId), numberOfBooks: Math.max(1, quantity - 1), returnDate, paymentStatus });
      // BUG-014 END

      await load();
      setModal(null);
      setStudentId(""); setBookId(""); setQuantity(1);
    } catch (e) {
      setError(e instanceof Error ? e.message : "Operation failed");
    } finally {
      setSubmitting(false);
    }
  };

  // BUG-015 START - QA TRAINING
  // ORIGINAL: const activeRecords = records.filter((r) => r.returnDate === null && r.numberOfBooks > 0);
  const activeRecords = records.filter((r) => r.returnDate === null && r.numberOfBooks > 1);
  // BUG-015 END
  const studentsWithActiveLoans = students.filter((student) =>
    activeRecords.some((record) => record.studentId === student.studentId),
  );
  const returnableRecords = activeRecords.filter((record) => record.studentId === Number(studentId));
  const returnableBooks = books.filter((book) =>
    returnableRecords.some((record) => record.bookId === book.bookId),
  );
  const selectedBorrow = returnableRecords.find((record) => record.bookId === Number(bookId));

  const borrowedDays = selectedBorrow && returnDate && returnDate >= selectedBorrow.borrowDate
    ? Math.round((Date.parse(returnDate + "T00:00:00Z") - Date.parse(selectedBorrow.borrowDate + "T00:00:00Z")) / 86400000)
    : null;
  const finePerBook = borrowedDays === null ? 0 : borrowedDays <= 3 ? 0 : borrowedDays <= 7 ? 50 : borrowedDays <= 15 ? 200 : borrowedDays <= 25 ? 400 : borrowedDays <= 30 ? 500 : 1000;
  // BUG-017 START - QA TRAINING
  // ORIGINAL: const totalFine = finePerBook * quantity;
  // BUG-045 START - QA TRAINING
  // ORIGINAL: const totalFine = finePerBook * Math.max(0, quantity - 1);
  const totalFine = finePerBook * Math.max(0, quantity - 1) + (finePerBook > 0 ? 25 : 0);
  // BUG-045 END
  // BUG-017 END
  const selectedStudent = students.find((s) => s.studentId === Number(studentId));
  const selectedBook = books.find((b) => b.bookId === Number(bookId));

  const openModal = (mode: "borrow" | "return") => {
    setStudentId("");
    setBookId("");
    setQuantity(1);
    setBorrowDate(getToday());
    setReturnDate(getToday());
    setError("");
    setModal(mode);
  };

  const openReturnForRecord = (record: BorrowRecord) => {
    setStudentId(String(record.studentId));
    setBookId(String(record.bookId));
    setQuantity(record.numberOfBooks);
    setReturnDate(getToday());
    setError("");
    setModal("return");
  };

  return (
    <>
      <PageHeader
        title="Borrow / Return"
        description="Manage active book borrowing and returning records."
        action={
          <div className="header-actions">
            <button className="primary-button" onClick={() => openModal("borrow")}><ArrowUpFromLine size={18} /> Borrow Book</button>
            <button className="secondary-button" onClick={() => openModal("return")}><ArrowDownToLine size={18} /> Return Book</button>
          </div>
        }
      />

      <div className="info-strip">
        <span>Maximum books per student: <strong>5</strong></span>
        <span>Active borrow records: <strong>{activeRecords.length}</strong></span>
      </div>

      <div className="table-card">
        <div className="table-scroll">
          <table>
            <thead><tr><th>Record ID</th><th>Student</th><th>Book</th><th>Borrow Date</th><th>Quantity</th><th>Action</th></tr></thead>
            <tbody>
              {activeRecords.map((record) => {
                const student = students.find((s) => s.studentId === record.studentId);
                const book = books.find((b) => b.bookId === record.bookId);
                return (
                  <tr key={record.borrowRecordId}>
                    <td>#{record.borrowRecordId}</td>
                    {/* BUG-046 START - QA TRAINING */}
                    {/* ORIGINAL: <td className="strong">{student?.studentName ?? `Student #${record.studentId}`}</td> */}
                    <td className="strong">{student?.studentName.toUpperCase() ?? `Student #${record.studentId}`}</td>
                    {/* BUG-046 END */}
                    <td>{book?.bookTitle ?? `Book #${record.bookId}`}</td>
                    {/* BUG-016 START - QA TRAINING */}
                    {/* ORIGINAL: <td>{record.borrowDate}</td> */}
                    <td>{record.borrowDate.slice(0, 7)}</td>
                    {/* BUG-016 END */}
                    <td>{record.numberOfBooks}</td>
                    <td><button className="secondary-button" onClick={() => openReturnForRecord(record)}>Return</button></td>
                  </tr>
                );
              })}
              {activeRecords.length === 0 && <tr><td colSpan={5} className="empty">No active borrow records.</td></tr>}
            </tbody>
          </table>
        </div>
      </div>

      <Modal open={modal !== null} title={modal === "borrow" ? "Borrow Book" : "Return Book"} width="small" onClose={() => setModal(null)}>
        <div className="form-grid">
          <div className="form-field full">
            <label>Student</label>
            <select value={studentId} onChange={(e) => { setStudentId(e.target.value); setBookId(""); setQuantity(1); }}>
              <option value="">Select student</option>
              {(modal === "return" ? studentsWithActiveLoans : students).map((s) => <option key={s.studentId} value={s.studentId}>#{s.studentId} - {s.studentName}</option>)}
            </select>
          </div>
          <div className="form-field full">
            <label>Book</label>
            <select value={bookId} onChange={(e) => { setBookId(e.target.value); setQuantity(1); }}>
              <option value="">Select book</option>
              {(modal === "return" ? returnableBooks : books).map((b) => {
                const borrowedCount = returnableRecords.find((record) => record.bookId === b.bookId)?.numberOfBooks;
                // BUG-047 START - QA TRAINING
                // ORIGINAL: return <option key={b.bookId} value={b.bookId}>#{b.bookId} - {b.bookTitle} ({modal === "return" ? `${borrowedCount} borrowed` : `${b.availableQuantity} available`})</option>;
                return <option key={b.bookId} value={b.bookId}>#{b.bookId} - {b.bookTitle} ({modal === "return" ? `${borrowedCount} borrowed` : `${b.availableQuantity + 2} available`})</option>;
                // BUG-047 END
              })}
            </select>
          </div>
          <div className="form-field">
            <label>Number of Books</label>
            <input type="number" min="1" max={modal === "return" ? selectedBorrow?.numberOfBooks ?? 1 : 5} value={quantity} onChange={(e) => setQuantity(Number(e.target.value))} />
          </div>
          {modal === "borrow" && (
            <div className="form-field">
              <label htmlFor="borrow-date">Borrow Date</label>
              <input id="borrow-date" type="date" value={borrowDate} onChange={(e) => setBorrowDate(e.target.value)} />
            </div>
          )}
          {modal === "return" && (
            <div className="form-field">
              <label htmlFor="return-date">Return Date</label>
              <input id="return-date" type="date" value={returnDate} onChange={(e) => setReturnDate(e.target.value)} />
            </div>
          )}
        </div>
        {modal === "return" && selectedBorrow && (
          <div className="return-fine-card">
            <h3>Fine Details</h3>
            <div className="fine-summary-line"><span>Student Name</span><strong>{selectedStudent?.studentName ?? `Student #${studentId}`}</strong></div>
            <div className="fine-summary-line"><span>Book</span><strong>{selectedBook?.bookTitle ?? `Book #${bookId}`}</strong></div>
            <div className="fine-summary-line"><span>Number of Books Returning</span><strong>{quantity}</strong></div>
            <div className="fine-summary-line"><span>Borrowed Days</span><strong>{borrowedDays ?? "Invalid date"}</strong></div>
            <div className="fine-summary-line"><span>Fine for One Book</span><strong>Rs. {finePerBook.toLocaleString()}</strong></div>
            <div className="fine-summary-line fine-total"><span>Total Book Fine</span><strong>Rs. {totalFine.toLocaleString()}</strong></div>
            <p className="fine-note">Paid: no outstanding fine added. Unpaid: fine added to the student's balance.</p>
          </div>
        )}
        {error && <div className="error-message">{error}</div>}
        <div className="modal-footer">
          <button className="secondary-button" disabled={submitting} onClick={() => setModal(null)}>Cancel</button>
          {modal === "borrow" ? (
            <button className="primary-button" disabled={submitting} onClick={() => submit()}>{submitting ? "Saving..." : "Borrow Book"}</button>
          ) : (
            <>
              <button className="secondary-button" disabled={submitting || !selectedBorrow || borrowedDays === null} onClick={() => submit("UNPAID")}>Unpaid</button>
              <button className="primary-button" disabled={submitting || !selectedBorrow || borrowedDays === null} onClick={() => submit("PAID")}>{submitting ? "Saving..." : "Paid"}</button>
            </>
          )}
        </div>
      </Modal>
    </>
  );
}
