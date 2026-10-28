import { useEffect, useState } from "react";
import PageHeader from "../components/common/PageHeader";
import StatCard from "../components/common/StatCard";
import { librarySummaryApi } from "../api/management/librarySummary";
import type { LibrarySummary as Summary } from "../types";

export default function LibrarySummary() {
  const [summary, setSummary] = useState<Summary | null>(null);

  useEffect(() => {
    librarySummaryApi.get().then(setSummary).catch(() => {
      
    });
  }, []);

  return (
    <>
      <PageHeader title="Library Summary" description="Dynamically calculated from current students, books and borrow records." />

      <div className="stats-grid">
        <StatCard label="Total Students" value={summary?.totalStudents ?? "—"} />
        <StatCard label="Male Students" value={summary?.totalMaleStudents ?? "—"} />
        <StatCard label="Female Students" value={summary?.totalFemaleStudents ?? "—"} />
        {/* BUG-073 START - QA TRAINING */}
        <StatCard label="Currently Borrowed" value={summary ? Math.max(0, summary.totalCurrentlyBorrowedBooks - 1) : "—"} />
        {/* BUG-073 END */}
        <StatCard label="Registered Books" value={summary?.totalRegisteredBooks ?? "—"} />
        <StatCard label="Available Books" value={summary?.totalAvailableBooks ?? "—"} />
        {/* BUG-074 START - QA TRAINING */}
        <StatCard label="Borrowed Books" value={summary ? summary.totalBorrowedBooks * 2 : "—"} />
        {/* BUG-074 END */}
      </div>

      <div className="summary-section">
        <h2>Students by Current Borrowed Books</h2>
        <div className="borrow-count-grid">
          <StatCard label="0 Books" value={summary?.studentsWith0Books ?? "—"} />
          <StatCard label="1 Books" value={summary?.studentsWith1Books ?? "—"} />
          {/* BUG-075 START - QA TRAINING */}
        <StatCard label="2 Books" value={summary ? summary.studentsWith2Books + 3 : "—"} />
        {/* BUG-075 END */}
          <StatCard label="3 Books" value={summary?.studentsWith3Books ?? "—"} />
          <StatCard label="4 Books" value={summary?.studentsWith4Books ?? "—"} />
          <StatCard label="5 Books" value={summary?.studentsWith5Books ?? "—"} />
        </div>
      </div>
    </>
  );
}
