using System.Diagnostics.CodeAnalysis;
using Retrospector.MediaTab.Interfaces;

namespace Retrospector.Tests.TestDoubles
{
    [SuppressMessage("ReSharper", "InconsistentNaming")]
    public class MediaTab_TestDouble : IMediaTab
    {
        public int CountOfCallsTo_Load { get; set; }
        public int LastIdPassedTo_Load { get; set; }
        public void Load(int mediaId)
        {
            CountOfCallsTo_Load++;
            LastIdPassedTo_Load = mediaId;
        }

        public int CountOfCallsTo_New { get; set; }
        public void New()
        {
            CountOfCallsTo_New++;
        }
    }
}