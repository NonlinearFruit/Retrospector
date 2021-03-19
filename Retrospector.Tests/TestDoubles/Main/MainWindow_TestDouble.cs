using System;
using System.Diagnostics.CodeAnalysis;
using Retrospector.Main.Interfaces;

namespace Retrospector.Tests.TestDoubles.Main
{
    [SuppressMessage("ReSharper", "InconsistentNaming")]
    public class MainWindow_TestDouble : IMainWindow
    {
        public int CountOfCallsTo_Show { get; set; }
        public Exception ExceptionToThrowFor_Show { get; set; }
        public void Show()
        {
            CountOfCallsTo_Show++;
            if (ExceptionToThrowFor_Show != null) throw ExceptionToThrowFor_Show;
        }

        public int CountOfCallsTo_Close { get; set; }
        public void Close()
        {
            CountOfCallsTo_Close++;
        }
    }
}