using System.Diagnostics.CodeAnalysis;

namespace Retrospector.DataStorage.Tests.Utilities
{
    [SuppressMessage("ReSharper", "InconsistentNaming")]
    public class Mapper_TestDouble<TModel, TEntity>
    {
        public TModel ReturnFor_ToModel { get; set; }
        public TEntity LastEntityPassedTo_ToModel { get; set; }
        public int CountOf_ToModel_Calls { get; set; }
        public TModel ToModel(TEntity entity)
        {
            CountOf_ToModel_Calls++;
            LastEntityPassedTo_ToModel = entity;
            return ReturnFor_ToModel;
        }

        public TEntity ReturnFor_ToEntity { get; set; }
        public TModel LastModelPassedTo_ToEntity { get; set; }
        public int CountOf_ToEntity_Calls { get; set; }
        public TEntity ToEntity(TModel model)
        {
            CountOf_ToEntity_Calls++;
            LastModelPassedTo_ToEntity = model;
            return ReturnFor_ToEntity;
        }
    }
}