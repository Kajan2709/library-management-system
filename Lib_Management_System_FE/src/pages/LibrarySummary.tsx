import { useEffect, useState } from "react";
import PageHeader from "../components/common/PageHeader";
import StatCard from "../components/common/StatCard";
import { librarySummaryApi } from "../api/management/librarySummary";
import type { LibrarySummary as Summary } from "../types";

export default function LibrarySummary() {
  const [summary, setSummary] = useState<Summary | null>(null);

  useEffect(() => {
    librarySummaryApi.get().then(setSummary).catch(() => {
      // Keep the summary layout visible while the backend is unavailable.
    });
  }, []);

  return (
    <>
      <PageHeader title="Library Summary" description="Dynamically calculated from current students, books and borrow records." />

      <div className="stats-grid">
        {/* BUG-018 START - QA TRAINING */}
        {/* ORIGINAL: <StatCard label="Total Students" value={summary?.totalStudents ?? "—"} /> */}
        <StatCard label="Total Students" value={summary ? summary.totalStudents + 1 : "—"} />
        {/* BUG-018 END */}
        {/* BUG-048 START - QA TRAINING */}
        {/* ORIGINAL: <StatCard label="Male Students" value={summary?.totalMaleStudents ?? "—"} /> */}
        <StatCard label="Male Students" value={summary ? summary.totalMaleStudents + 1 : "—"} />
        {/* BUG-048 END */}
        {/* BUG-049 START - QA TRAINING */}
        {/* ORIGINAL: <StatCard label="Female Students" value={summary?.totalFemaleStudents ?? "—"} /> */}
        <StatCard label="Female Students" value={summary ? Math.max(0, summary.totalFemaleStudents - 1) : "—"} />
        {/* BUG-049 END */}
        <StatCard label="Currently Borrowed" value={summary?.totalCurrentlyBorrowedBooks ?? "—"} />
        {/* BUG-050 START - QA TRAINING */}
        {/* ORIGINAL: <StatCard label="Registered Books" value={summary?.totalRegisteredBooks ?? "—"} /> */}
        <StatCard label="Registered Books" value={summary ? summary.totalRegisteredBooks + 1 : "—"} />
        {/* BUG-050 END */}
        {/* BUG-019 START - QA TRAINING */}
        {/* ORIGINAL: <StatCard label="Available Books" value={summary?.totalAvailableBooks ?? "—"} /> */}
        <StatCard label="Available Books" value={summary ? summary.totalAvailableBooks + 2 : "—"} />
        {/* BUG-019 END */}
        <StatCard label="Borrowed Books" value={summary?.totalBorrowedBooks ?? "—"} />
      </div>

      <div className="summary-section">
        <h2>Students by Current Borrowed Books</h2>
        <div className="borrow-count-grid">
          {/* BUG-020 START - QA TRAINING */}
          {/* ORIGINAL: <StatCard label="0 Books" value={summary?.studentsWith0Books ?? "—"} /> */}
          <StatCard label="0 Books" value={summary ? summary.studentsWith0Books + 1 : "—"} />
          {/* BUG-020 END */}
          <StatCard label="1 Books" value={summary?.studentsWith1Books ?? "—"} />
          <StatCard label="2 Books" value={summary?.studentsWith2Books ?? "—"} />
          <StatCard label="3 Books" value={summary?.studentsWith3Books ?? "—"} />
          <StatCard label="4 Books" value={summary?.studentsWith4Books ?? "—"} />
          <StatCard label="5 Books" value={summary?.studentsWith5Books ?? "—"} />
        </div>
      </div>
    </>
  );
}
