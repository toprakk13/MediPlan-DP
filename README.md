# 🩺 MediPlan DP: Medical Diagnostic Cost & Patient Burden Optimizer

**MediPlan DP** is a Java-based analysis system that models complex medical diagnostic catalogues, clinical dependencies, and sampling procedures. It leverages **Dynamic Programming (DP)** and **Graph Algorithms** to evaluate and optimize both **Patient Burden** and **Hospital Costs**.

---

## 📌 Problem Overview & Core Concepts

Medical diagnostic tests are categorized into three core types:
1. **RAW Tests:** Involve direct physical sampling from patients (e.g., Blood, Urine, Tissue). Examples include *Total Cholesterol* and *Blood Glucose*.
2. **DERIVED Tests:** Analytical assessments computed solely from raw diagnostic data.
3. **COMPOSITE Tests:** High-level clinical indices calculated by aggregating multiple derived or composite metrics (e.g., *Overall Health Index*).

### 💡 Key Problems Solved
* **Patient Burden Analysis (Top-Down DP):** Identifies the exact count of unique invasive procedures (blood draws, tissue biopsies, etc.) a patient must undergo for a targeted health score.
* **Hospital Cost Analysis (Bottom-Up DP):** Resolves test dependencies to compute baseline costs and prevent redundant test calculations.
* **Execution Plan Optimization (Traceback):** Traverses the Directed Acyclic Graph (DAG) of test dependencies to eliminate duplicate subtree costs, producing an optimal execution sequence and reducing overall hospital costs.

---

## 🏗️ Algorithmic Approach

The system models tests as nodes in a Directed Acyclic Graph (DAG) and solves cost and burden propagation using Dynamic Programming:
