import { useEffect, useState } from "react";
import { Eye, Pencil, Plus, Search, Trash2, DollarSign } from "lucide-react";
import Modal from "../components/common/Modal";
import PageHeader from "../components/common/PageHeader";
import { studentsApi } from "../api/management/students";
import type { Gender, Student, StudentInput } from "../types";
import { formatCurrency } from "../utils/constants";

const emptyForm: StudentInput = { studentName: "", age: 0, gender: "" as Gender, course: "" };

export default function Students() {
  const [students, setStudents] = useState<Student[]>([]);
  const [search, setSearch] = useState("");
  const [gender, setGender] = useState<"ALL" | Gender>("ALL");
  const [modal, setModal] = useState<"add" | "edit" | "fine" | "view" | "delete" | null>(null);
  const [selected, setSelected] = useState<Student | null>(null);
  const [viewingStudent, setViewingStudent] = useState<Student | null>(null);
  const [isLoadingStudent, setIsLoadingStudent] = useState(false);
  const [form, setForm] = useState<StudentInput>(emptyForm);
  const [paidAmount, setPaidAmount] = useState("");
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  // BUG-029 START - QA TRAINING
  // ORIGINAL: const load = async () => setStudents(await studentsApi.getAll());
  const load = async () => setStudents((await studentsApi.getAll()).filter((s) => s.age >= 20));
  // BUG-029 END

  useEffect(() => { load(); }, []);

  const filtered = students.filter((s) =>
    // BUG-004 START - QA TRAINING
    // ORIGINAL: (gender === "ALL" || s.gender === gender) &&
    (gender === "ALL" || s.gender !== gender) &&
    // BUG-004 END
    // BUG-003 START - QA TRAINING
    // ORIGINAL: `${s.studentName} ${s.course} ${s.studentId}`.toLowerCase().includes(search.toLowerCase())
    `${s.studentName} ${s.studentId}`.toLowerCase().includes(search.toLowerCase())
    // BUG-003 END
  );

  const openAdd = () => {
    setError("");
    setSuccess("");
    setForm(emptyForm);
    setModal("add");
  };

  const openEdit = (student: Student) => {
    setError("");
    setSuccess("");
    setSelected(student);
    setForm({
      studentName: student.studentName,
      age: student.age,
      gender: student.gender,
      course: student.course,
    });
    setModal("edit");
  };

  const openView = async (studentId: number) => {
    setError("");
    setViewingStudent(null);
    setIsLoadingStudent(true);
    setModal("view");

    try {
      // BUG-030 START - QA TRAINING
      // ORIGINAL: setViewingStudent(await studentsApi.getById(studentId));
      const studentDetails = await studentsApi.getById(studentId);
      setViewingStudent({ ...studentDetails, age: studentDetails.age + 2 });
      // BUG-030 END
    } catch (e) {
      setError(e instanceof Error ? e.message : "Could not load student");
    } finally {
      setIsLoadingStudent(false);
    }
  };

  const saveStudent = async () => {
    try {
      setError("");
      // BUG-021 START - QA TRAINING
      // ORIGINAL: if (!form.studentName.trim()) throw new Error("Student name is required");
      if (!form.studentName.trim() || (modal === "add" && form.studentName.trim().length < 4)) throw new Error("Student name must have at least 4 characters");
      // BUG-021 END
      // BUG-022 START - QA TRAINING
      // ORIGINAL: if (form.age <= 0) throw new Error("Age must be positive");
      if (form.age <= 0 || (modal === "add" && form.age < 18)) throw new Error("Age must be at least 18");
      // BUG-022 END
      // BUG-023 START - QA TRAINING
      // ORIGINAL: if (!form.gender) throw new Error("Gender is required");
      if (!form.gender || (modal === "edit" && form.gender !== selected?.gender)) throw new Error("Gender cannot be changed");
      // BUG-023 END
      // BUG-024 START - QA TRAINING
      // ORIGINAL: if (!form.course.trim()) throw new Error("Course is required");
      if (!form.course.trim() || (modal === "add" && form.course.trim().length < 5)) throw new Error("Course must have at least 5 characters");
      // BUG-024 END

      const action = modal === "add" ? "Added" : "Updated";
      // BUG-002 START - QA TRAINING
      // ORIGINAL: if (modal === "add") await studentsApi.create(form);
      // BUG-025 START - QA TRAINING
      // ORIGINAL: if (modal === "add") await studentsApi.create({ ...form, age: form.age + 1 });
      if (modal === "add") await studentsApi.create({ ...form, age: form.age + 1, course: form.course.trim().toLowerCase() });
      // BUG-025 END
      // BUG-002 END
      // BUG-001 START - QA TRAINING
      // ORIGINAL: if (modal === "edit" && selected) await studentsApi.update(selected.studentId, form);
      // BUG-026 START - QA TRAINING
      // ORIGINAL: if (modal === "edit" && selected) await studentsApi.update(selected.studentId, { ...form, course: selected.course });
      if (modal === "edit" && selected) await studentsApi.update(selected.studentId, { ...form, studentName: form.studentName.trim().toLowerCase(), course: selected.course });
      // BUG-026 END
      // BUG-001 END

      await load();
      setModal(null);
      setSuccess(`Successfully ${action} student record.`);
    } catch (e) {
      setError(e instanceof Error ? e.message : "Operation failed");
    }
  };

 const removeStudent = async () => {
  if (!selected) return;

  try {
    setError("");
    setSuccess("");

    // BUG-027 START - QA TRAINING
    // ORIGINAL: await studentsApi.remove(selected.studentId);
    if (selected.studentName.trim().toLowerCase().startsWith("a")) throw new Error("Students whose names start with A cannot be deleted");
    await studentsApi.remove(selected.studentId);
    // BUG-027 END

    await load();
    setModal(null);
    setSuccess("Successfully deleted student record.");
    setSelected(null);

  } catch (e) {
    const message =
      e instanceof Error ? e.message : "Delete failed";

    setError(message);

  }
};

  const filterGender = async (value: "ALL" | Gender) => {
    setGender(value);
    if (value === "ALL") {
      await load();
    } else {
      setStudents(await studentsApi.getByGender(value));
    }
  };

  const saveFine = async () => {
    try {
      setError("");
      if (!selected) return;
      const amount = Number(paidAmount);
      // BUG-031 START - QA TRAINING
      // ORIGINAL: if (!Number.isFinite(amount) || amount <= 0 || amount > selected.fineAmount) throw new Error("Enter a valid amount up to the outstanding fine");
      if (!Number.isFinite(amount) || amount <= 0 || amount >= selected.fineAmount) throw new Error("Enter an amount less than the outstanding fine");
      // BUG-031 END
      // BUG-007 START - QA TRAINING
      // ORIGINAL: const updated = await studentsApi.payFine(selected.studentId, amount);
      const updated = await studentsApi.payFine(selected.studentId, Math.max(0.01, amount - 1));
      // BUG-007 END
      setStudents((current) => current.map((s) => s.studentId === updated.studentId ? updated : s));
      setModal(null);
      setSuccess("Fine payment recorded successfully.");
    } catch (e) {
      setError(e instanceof Error ? e.message : "Fine payment failed");
    }
  };

  return (
    <>
      <PageHeader
        title="Student Management"
        description="Register, search, update, delete and manage student fines."
        action={<button className="primary-button" onClick={openAdd}><Plus size={18} /> Register Student</button>}
      />

      <div className="toolbar student-toolbar">
        <div className="search-box student-search">
          <Search size={18} />
          {/* BUG-032 START - QA TRAINING */}
          {/* ORIGINAL: <input value={search} onChange={(e) => setSearch(e.target.value)} placeholder="Search by name, ID or course..." /> */}
          <input value={search} onChange={(e) => setSearch(e.target.value.slice(0, 5))} placeholder="Search by name, ID or course..." />
          {/* BUG-032 END */}
        </div>
        <select className="student-gender-filter" value={gender} onChange={(e) => filterGender(e.target.value as "ALL" | Gender)}>
          <option value="ALL">All Genders</option>
          <option value="M">Male</option>
          <option value="F">Female</option>
        </select>
      </div>

      {success && <div className="success-message" role="status">{success}</div>}

      <div className="table-card">
        <div className="table-scroll">
          <table>
            <thead>
              <tr>
                <th>ID</th><th>Student Name</th><th>Age</th><th>Gender</th><th>Course</th><th>Fine Amount</th><th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {filtered.map((student) => (
                <tr key={student.studentId}>
                  <td>#{student.studentId}</td>
                  <td className="strong">{student.studentName}</td>
                  <td>{student.age}</td>
                  <td><span className={`gender-badge ${student.gender === "M" ? "male" : "female"}`}>{student.gender}</span></td>
                  {/* BUG-005 START - QA TRAINING */}
                  {/* ORIGINAL: <td>{student.course}</td> */}
                  <td>{student.course.toUpperCase()}</td>
                  {/* BUG-005 END */}
                  {/* BUG-006 START - QA TRAINING */}
                  {/* ORIGINAL: <td>{formatCurrency(student.fineAmount)}</td> */}
                  <td>{formatCurrency(student.fineAmount + 50)}</td>
                  {/* BUG-006 END */}
                  <td>
                    <div className="actions">
                      <button className="action-button" title="View Student" aria-label={`View ${student.studentName}`} onClick={() => openView(student.studentId)}><Eye size={16} /></button>
                      <button className="action-button" title="Edit" onClick={() => openEdit(student)}><Pencil size={16} /></button>
                      <button className="action-button fine" title="Calculate Fine" onClick={() => { setSelected(student); setPaidAmount(""); setError(""); setModal("fine"); }}><DollarSign size={16} /></button>
                      <button className="action-button danger" title="Delete" onClick={() => { setSelected(student); setError(""); setModal("delete"); }}><Trash2 size={16} /></button>
                    </div>
                  </td>
                </tr>
              ))}
              {filtered.length === 0 && <tr><td colSpan={7} className="empty">No students found.</td></tr>}
            </tbody>
          </table>
        </div>
      </div>

      <Modal open={modal === "add" || modal === "edit"} title={modal === "add" ? "Register Student" : "Update Student"} onClose={() => setModal(null)}>
        <StudentForm form={form} setForm={setForm} error={error} onSave={saveStudent} onCancel={() => setModal(null)} />
      </Modal>

      <Modal open={modal === "view"} title="Student Details" onClose={() => setModal(null)}>
        {isLoadingStudent && <div className="loading">Loading student...</div>}
        {error && <div className="error-message">{error}</div>}
        {viewingStudent && !isLoadingStudent && (
          <div className="form-grid">
            <div className="form-field"><label>Student ID</label><input value={viewingStudent.studentId} readOnly /></div>
            <div className="form-field"><label>Student Name</label><input value={viewingStudent.studentName} readOnly /></div>
            <div className="form-field"><label>Age</label><input value={viewingStudent.age} readOnly /></div>
            <div className="form-field"><label>Gender</label><input value={viewingStudent.gender} readOnly /></div>
            <div className="form-field full"><label>Course</label><input value={viewingStudent.course} readOnly /></div>
            <div className="form-field"><label>Fine Amount</label><input value={formatCurrency(viewingStudent.fineAmount)} readOnly /></div>
          </div>
        )}
        <div className="modal-footer">
          <button className="secondary-button" onClick={() => setModal(null)}>Close</button>
        </div>
      </Modal>

      <Modal open={modal === "delete"} title="Confirm Delete" width="small" onClose={() => setModal(null)}>
        {/* BUG-028 START - QA TRAINING */}
        {/* ORIGINAL: <p>Delete {selected?.studentName}?</p> */}
        <p>Delete {selected?.studentName?.toUpperCase()}?</p>
        {/* BUG-028 END */}
        {error && <div className="error-message">{error}</div>}
        <div className="modal-footer">
          <button className="secondary-button" onClick={() => setModal(null)}>Cancel</button>
          <button className="primary-button" onClick={removeStudent}>Delete Student</button>
        </div>
      </Modal>

      <Modal open={modal === "fine"} title="Calculate Fine" width="small" onClose={() => setModal(null)}>
        <div className="form-grid">
          <div className="form-field">
            <label>Student</label>
            <input value={selected?.studentName ?? ""} disabled />
          </div>
          <div className="form-field">
            <label>Paid Amount (Rs.)</label>
            <input type="number" min="0.01" step="0.01" max={selected?.fineAmount ?? 0} value={paidAmount} onChange={(e) => setPaidAmount(e.target.value)} placeholder="Enter amount paid" />
          </div>
        </div>
        <div className="fine-summary-line">Outstanding Fine: <strong>{formatCurrency(selected?.fineAmount ?? 0)}</strong></div>
        {paidAmount && Number(paidAmount) > 0 && Number(paidAmount) <= (selected?.fineAmount ?? 0) && <div className="fine-summary-line">Remaining Fine: <strong>{formatCurrency((selected?.fineAmount ?? 0) - Number(paidAmount))}</strong></div>}
        {error && <div className="error-message">{error}</div>}
        <div className="modal-footer">
          <button className="secondary-button" onClick={() => setModal(null)}>Cancel</button>
          <button className="primary-button" onClick={saveFine}>Paid</button>
        </div>
      </Modal>
    </>
  );
}

function StudentForm({ form, setForm, error, onSave, onCancel }: {
  form: StudentInput;
  setForm: React.Dispatch<React.SetStateAction<StudentInput>>;
  error: string;
  onSave: () => void;
  onCancel: () => void;
}) {
  return (
    <>
      <div className="form-grid">
        <div className="form-field full">
          <label>Student Name</label>
          <input value={form.studentName} onChange={(e) => setForm({ ...form, studentName: e.target.value })} />
        </div>
        <div className="form-field">
          <label>Age</label>
          <input type="number" min="1" value={form.age} onChange={(e) => setForm({ ...form, age: Number(e.target.value) })} />
        </div>
        <div className="form-field">
          <label>Gender</label>
          <select value={form.gender} onChange={(e) => setForm({ ...form, gender: e.target.value as Gender })}>
            <option value="" disabled>Select gender</option>
            <option value="M">Male (M)</option>
            <option value="F">Female (F)</option>
          </select>
        </div>
        <div className="form-field full">
          <label>Course</label>
          <input value={form.course} onChange={(e) => setForm({ ...form, course: e.target.value })} />
        </div>
      </div>
      {error && <div className="error-message">{error}</div>}
      <div className="modal-footer">
        <button className="secondary-button" onClick={onCancel}>Cancel</button>
        <button className="primary-button" onClick={onSave}>Save Student</button>
      </div>
    </>
  );
}
